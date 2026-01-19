package dev.imrob.painelconsulta.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.imrob.painelconsulta.domain.Pagamento;
import dev.imrob.painelconsulta.domain.Plano;
import dev.imrob.painelconsulta.domain.User;
import dev.imrob.painelconsulta.dto.request.CheckoutRequest;
import dev.imrob.painelconsulta.dto.request.UserRegistrationRequest;
import dev.imrob.painelconsulta.dto.response.CheckoutResponse;
import dev.imrob.painelconsulta.exception.BusinessException;
import dev.imrob.painelconsulta.repository.PagamentoRepository;
import dev.imrob.painelconsulta.repository.PlanoRepository;
import dev.imrob.painelconsulta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final UserRepository userRepository;
    private final PlanoRepository planoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Transactional
    public CheckoutResponse iniciarCheckout(CheckoutRequest request) {
        Plano plano = planoRepository.findById(request.planoId())
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        // Verificar se usuário já existe
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user == null) {
            // Criar novo usuário inativo
            user = User.builder()
                    .nome(request.nome())
                    .email(request.email())
                    .telefone(request.telefone())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 8))) // Senha temporária
                    .role("ROLE_USER")
                    .active(false)
                    .planoId(request.planoId())
                    .consultasUsadas(0)
                    .createdAt(LocalDateTime.now())
                    .build();
            user = userRepository.save(user);
        }

        // Gerar código PIX
        String codigoPix = gerarCodigoPix(plano, user);

        // Criar registro de pagamento pendente
        Pagamento pagamento = Pagamento.builder()
                .userId(user.getId())
                .planoId(plano.getId())
                .valor(plano.getPreco())
                .codigoPix(codigoPix)
                .status("PENDENTE")
                .createdAt(LocalDateTime.now())
                .build();

        pagamento = pagamentoRepository.save(pagamento);

        // Gerar QR Code
        String qrCodeBase64 = gerarQrCodeBase64(codigoPix);

        return new CheckoutResponse(
                pagamento.getId(),
                codigoPix,
                qrCodeBase64,
                "PENDENTE"
        );
    }

    public CheckoutResponse verificarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));

        return new CheckoutResponse(
                pagamento.getId(),
                pagamento.getCodigoPix(),
                null,
                pagamento.getStatus()
        );
    }

    @Transactional
    public CheckoutResponse confirmarPagamento(Long pagamentoId, String senha) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));

        if (!"PENDENTE".equals(pagamento.getStatus())) {
            throw new BusinessException("Pagamento já foi processado");
        }

        // Atualizar status do pagamento
        pagamento.setStatus("CONFIRMADO");
        pagamento.setConfirmedAt(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        // Ativar usuário e definir senha
        User user = userRepository.findById(pagamento.getUserId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Plano plano = planoRepository.findById(pagamento.getPlanoId())
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(senha));
        user.setPlanoId(plano.getId());
        user.setDataExpiracao(LocalDateTime.now().plusDays(plano.getValidadeDias()));
        user.setConsultasUsadas(0);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Pagamento {} confirmado. Usuário {} ativado com plano {}",
                pagamentoId, user.getEmail(), plano.getNome());

        return new CheckoutResponse(
                pagamento.getId(),
                pagamento.getCodigoPix(),
                null,
                "CONFIRMADO"
        );
    }

    // Simular confirmação de pagamento (para testes)
    @Transactional
    public void simularConfirmacaoPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));

        if (!"PENDENTE".equals(pagamento.getStatus())) {
            return;
        }

        pagamento.setStatus("CONFIRMADO");
        pagamento.setConfirmedAt(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        userService.ativarUsuario(pagamento.getUserId(), pagamento.getPlanoId());

        log.info("Pagamento {} simulado como confirmado", pagamentoId);
    }

    private String gerarCodigoPix(Plano plano, User user) {
        // Código PIX simulado no padrão EMV
        // Em produção, usar uma biblioteca adequada ou API de gateway de pagamento
        String chaveAleatoria = UUID.randomUUID().toString().replace("-", "").substring(0, 25);
        String valor = plano.getPreco().toString().replace(".", "");

        StringBuilder pix = new StringBuilder();
        pix.append("00020126"); // Payload Format Indicator
        pix.append("580014BR.GOV.BCB.PIX"); // Merchant Account Info
        pix.append("01").append(String.format("%02d", chaveAleatoria.length())).append(chaveAleatoria);
        pix.append("52040000"); // Merchant Category Code
        pix.append("5303986"); // Transaction Currency (BRL)
        pix.append("54").append(String.format("%02d", valor.length())).append(valor);
        pix.append("5802BR"); // Country Code
        pix.append("5913PAINELCONSULTA"); // Merchant Name
        pix.append("6008SAOPAULO"); // Merchant City
        pix.append("62070503***"); // Additional Data
        pix.append("6304"); // CRC placeholder

        return pix.toString();
    }

    private String gerarQrCodeBase64(String texto) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 300, 300);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (WriterException | IOException e) {
            log.error("Erro ao gerar QR Code", e);
            return "";
        }
    }
}
