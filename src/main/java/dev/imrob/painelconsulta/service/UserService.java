package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.domain.Plano;
import dev.imrob.painelconsulta.domain.User;
import dev.imrob.painelconsulta.dto.request.ChangePasswordRequest;
import dev.imrob.painelconsulta.dto.request.UserRegistrationRequest;
import dev.imrob.painelconsulta.dto.request.UserUpdateRequest;
import dev.imrob.painelconsulta.dto.response.DashboardStats;
import dev.imrob.painelconsulta.dto.response.UserResponse;
import dev.imrob.painelconsulta.exception.BusinessException;
import dev.imrob.painelconsulta.repository.PlanoRepository;
import dev.imrob.painelconsulta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlanoRepository planoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConsultaService consultaService;

    @Transactional
    public UserResponse registrarUsuario(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = User.builder()
                .nome(request.nome())
                .email(request.email())
                .telefone(request.telefone())
                .password(passwordEncoder.encode(request.password()))
                .role("ROLE_USER")
                .active(false) // Aguarda ativação via pagamento
                .planoId(request.planoId())
                .consultasUsadas(0)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        return toUserResponse(user);
    }

    @Transactional
    public UserResponse criarUsuarioAdmin(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        Plano plano = null;
        if (request.planoId() != null) {
            plano = planoRepository.findById(request.planoId()).orElse(null);
        }

        User user = User.builder()
                .nome(request.nome())
                .email(request.email())
                .telefone(request.telefone())
                .password(passwordEncoder.encode(request.password()))
                .role("ROLE_USER")
                .active(true)
                .planoId(request.planoId())
                .consultasUsadas(0)
                .dataExpiracao(plano != null ? LocalDateTime.now().plusDays(plano.getValidadeDias()) : null)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        return toUserResponse(user);
    }

    @Transactional
    public UserResponse atualizarUsuario(UserUpdateRequest request) {
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        // Verificar se email já existe para outro usuário
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado para outro usuário");
        }

        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setTelefone(request.telefone());

        if (request.role() != null) {
            user.setRole(request.role());
        }

        if (request.active() != null) {
            user.setActive(request.active());
        }

        if (request.planoId() != null && !request.planoId().equals(user.getPlanoId())) {
            user.setPlanoId(request.planoId());
            Plano plano = planoRepository.findById(request.planoId()).orElse(null);
            if (plano != null) {
                user.setDataExpiracao(LocalDateTime.now().plusDays(plano.getValidadeDias()));
                user.setConsultasUsadas(0);
            }
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        return toUserResponse(user);
    }

    @Transactional
    public void alterarSenha(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), user.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        if (!request.novaSenha().equals(request.confirmaSenha())) {
            throw new BusinessException("A nova senha e a confirmação não conferem");
        }

        user.setPassword(passwordEncoder.encode(request.novaSenha()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        return toUserResponse(user);
    }

    public UserResponse buscarPorEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        return toUserResponse(user);
    }

    public User getUsuarioEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

    public Page<UserResponse> listarUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }

    public Page<UserResponse> pesquisarUsuarios(String search, Pageable pageable) {
        return userRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable).map(this::toUserResponse);
    }

    public DashboardStats getDashboardStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Integer totalConsultas = consultaService.countTotalConsultas(userId);
        Integer consultasHoje = consultaService.countConsultasHoje(userId);

        Integer limiteConsultas = 0;
        if (user.getPlanoId() != null) {
            Plano plano = planoRepository.findById(user.getPlanoId()).orElse(null);
            if (plano != null) {
                limiteConsultas = plano.getLimiteConsultas();
            }
        }

        Integer consultasUsadas = user.getConsultasUsadas() == null ? 0 : user.getConsultasUsadas();
        Integer consultasRestantes = Math.max(0, limiteConsultas - consultasUsadas);

        Long diasRestantes = 0L;
        if (user.getDataExpiracao() != null && user.getDataExpiracao().isAfter(LocalDateTime.now())) {
            diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), user.getDataExpiracao());
        }

        return new DashboardStats(totalConsultas, consultasHoje, consultasRestantes, limiteConsultas, diasRestantes);
    }

    @Transactional
    public void ativarUsuario(Long userId, Long planoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        user.setActive(true);
        user.setPlanoId(planoId);
        user.setDataExpiracao(LocalDateTime.now().plusDays(plano.getValidadeDias()));
        user.setConsultasUsadas(0);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void renovarPlano(Long userId, Long planoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        LocalDateTime novaExpiracao;
        if (user.getDataExpiracao() != null && user.getDataExpiracao().isAfter(LocalDateTime.now())) {
            // Se ainda não expirou, adiciona dias a partir da data atual de expiração
            novaExpiracao = user.getDataExpiracao().plusDays(plano.getValidadeDias());
        } else {
            // Se já expirou, começa a contar de hoje
            novaExpiracao = LocalDateTime.now().plusDays(plano.getValidadeDias());
        }

        user.setPlanoId(planoId);
        user.setDataExpiracao(novaExpiracao);
        user.setConsultasUsadas(0);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void desativarUsuario(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserResponse toUserResponse(User user) {
        String planoNome = null;
        Integer limiteConsultas = null;

        if (user.getPlanoId() != null) {
            Plano plano = planoRepository.findById(user.getPlanoId()).orElse(null);
            if (plano != null) {
                planoNome = plano.getNome();
                limiteConsultas = plano.getLimiteConsultas();
            }
        }

        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole(),
                user.isActive() ? "ATIVO" : "INATIVO",
                planoNome,
                user.getConsultasUsadas(),
                limiteConsultas,
                user.getDataExpiracao()
        );
    }
}
