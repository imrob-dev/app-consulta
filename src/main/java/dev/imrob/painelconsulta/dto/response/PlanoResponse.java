package dev.imrob.painelconsulta.dto.response;

import java.math.BigDecimal;

public record PlanoResponse(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer limiteConsultas,
    Integer validadeDias,
    boolean active
) {}
