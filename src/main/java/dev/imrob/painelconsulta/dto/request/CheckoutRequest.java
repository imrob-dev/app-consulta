package dev.imrob.painelconsulta.dto.request;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
    @NotNull(message = "O plano é obrigatório")
    Long planoId,

    String nome,
    String email,
    String telefone
) {}
