package dev.imrob.painelconsulta.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConsultaRequest(
    @NotBlank(message = "O tipo da consulta é obrigatório")
    String tipo,

    @NotBlank(message = "O valor da consulta é obrigatório")
    String valor
) {}
