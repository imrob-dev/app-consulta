package dev.imrob.painelconsulta.dto.response;

public record DashboardStats(
    Integer totalConsultas,
    Integer consultasHoje,
    Integer consultasRestantes,
    Integer limiteConsultas,
    Long diasRestantes
) {}
