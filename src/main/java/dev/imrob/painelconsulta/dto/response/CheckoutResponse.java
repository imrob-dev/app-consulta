package dev.imrob.painelconsulta.dto.response;

public record CheckoutResponse(
    Long pagamentoId,
    String codigoPix,
    String qrCodeBase64,
    String status
) {}
