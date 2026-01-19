package dev.imrob.painelconsulta.dto.response;

import java.time.LocalDateTime;

public record ConsultaResponse(
    Long id,
    String tipo,
    String valorConsultado,
    Object dados,
    String status,
    LocalDateTime timestamp
) {}
