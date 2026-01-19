package dev.imrob.painelconsulta.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String nome,
    String email,
    String telefone,
    String role,
    String status,
    String planoNome,
    Integer consultasUsadas,
    Integer limiteConsultas,
    LocalDateTime dataExpiracao
) {}
