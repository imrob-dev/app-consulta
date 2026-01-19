package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Serviço delegador que escolhe entre Mock e API Real baseado na configuração do sistema.
 */
@Service
@Primary
@RequiredArgsConstructor
public class ConsultaApiDelegatorService implements ConsultaApiService {

    private final ConfiguracaoService configuracaoService;
    private final MockConsultaApiServiceImpl mockService;
    private final RealConsultaApiServiceImpl realService;

    @Override
    public ConsultaResponse executarConsulta(String tipo, String valor) {
        if (configuracaoService.isUsarMockConsultas()) {
            return mockService.executarConsulta(tipo, valor);
        }
        return realService.executarConsulta(tipo, valor);
    }
}
