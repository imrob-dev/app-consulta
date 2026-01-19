package dev.imrob.painelconsulta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.imrob.painelconsulta.domain.Consulta;
import dev.imrob.painelconsulta.domain.Plano;
import dev.imrob.painelconsulta.domain.User;
import dev.imrob.painelconsulta.dto.request.ConsultaRequest;
import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import dev.imrob.painelconsulta.exception.BusinessException;
import dev.imrob.painelconsulta.repository.ConsultaRepository;
import dev.imrob.painelconsulta.repository.PlanoRepository;
import dev.imrob.painelconsulta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaApiService consultaApiService;
    private final UserRepository userRepository;
    private final PlanoRepository planoRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ConsultaResponse realizarConsulta(Long userId, ConsultaRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        // Verificar se usuário está ativo
        if (!user.isActive()) {
            throw new BusinessException("Sua conta está inativa. Entre em contato com o suporte.");
        }

        // Verificar expiração do plano
        if (user.getDataExpiracao() != null && user.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Seu plano expirou. Por favor, renove seu acesso.");
        }

        // Verificar limite de consultas
        if (user.getPlanoId() != null) {
            Plano plano = planoRepository.findById(user.getPlanoId()).orElse(null);
            if (plano != null && user.getConsultasUsadas() >= plano.getLimiteConsultas()) {
                throw new BusinessException("Você atingiu o limite de consultas do seu plano.");
            }
        }

        // Executar consulta na API (mock ou real)
        ConsultaResponse response = consultaApiService.executarConsulta(request.tipo(), request.valor());

        // Salvar consulta no histórico
        Consulta consulta = Consulta.builder()
                .userId(userId)
                .tipo(request.tipo())
                .valor(request.valor())
                .resultado(serializeResult(response.dados()))
                .dataConsulta(LocalDateTime.now())
                .status(response.status())
                .build();

        consulta = consultaRepository.save(consulta);

        // Incrementar contador de consultas do usuário
        user.setConsultasUsadas((user.getConsultasUsadas() == null ? 0 : user.getConsultasUsadas()) + 1);
        userRepository.save(user);

        return new ConsultaResponse(
                consulta.getId(),
                response.tipo(),
                response.valorConsultado(),
                response.dados(),
                response.status(),
                consulta.getDataConsulta()
        );
    }

    public Page<Consulta> getHistorico(Long userId, Pageable pageable) {
        return consultaRepository.findByUserId(userId, pageable);
    }

    public Page<Consulta> getHistoricoByTipo(Long userId, String tipo, Pageable pageable) {
        return consultaRepository.findByUserIdAndTipo(userId, tipo, pageable);
    }

    public ConsultaResponse getConsultaById(Long userId, Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new BusinessException("Consulta não encontrada"));

        if (!consulta.getUserId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para visualizar esta consulta");
        }

        Object dados = deserializeResult(consulta.getResultado());

        return new ConsultaResponse(
                consulta.getId(),
                consulta.getTipo(),
                consulta.getValor(),
                dados,
                consulta.getStatus(),
                consulta.getDataConsulta()
        );
    }

    public Integer countConsultasHoje(Long userId) {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return (int) consultaRepository.countByUserIdAndDataConsultaGreaterThanEqual(userId, inicioDia);
    }

    public Integer countTotalConsultas(Long userId) {
        return (int) consultaRepository.countByUserId(userId);
    }

    private String serializeResult(Object dados) {
        try {
            return objectMapper.writeValueAsString(dados);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private Object deserializeResult(String resultado) {
        try {
            return objectMapper.readValue(resultado, Object.class);
        } catch (JsonProcessingException e) {
            return resultado;
        }
    }
}
