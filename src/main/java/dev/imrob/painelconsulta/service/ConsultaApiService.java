package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.dto.response.ConsultaResponse;

public interface ConsultaApiService {
    ConsultaResponse executarConsulta(String tipo, String valor);
}
