package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RealConsultaApiServiceImpl implements ConsultaApiService {

    private final RestClient restClient;

    public RealConsultaApiServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.externa.com/v1") // URL da API externa real
                .build();
    }

    @Override
    public ConsultaResponse executarConsulta(String tipo, String valor) {
        try {
            String endpoint = switch (tipo.toUpperCase()) {
                case "CPF" -> "/cpf/" + valor;
                case "CNPJ" -> "/cnpj/" + valor;
                case "PLACA" -> "/placa/" + valor;
                case "TELEFONE" -> "/telefone/" + valor;
                case "EMAIL" -> "/email/" + valor;
                case "NOME" -> "/nome/" + valor;
                default -> throw new IllegalArgumentException("Tipo de consulta não suportado: " + tipo);
            };

            @SuppressWarnings("unchecked")
            Map<String, Object> dados = restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(Map.class);

            return new ConsultaResponse(null, tipo, valor, dados, "SUCESSO", LocalDateTime.now());

        } catch (Exception e) {
            return new ConsultaResponse(null, tipo, valor,
                Map.of("erro", e.getMessage()), "ERRO", LocalDateTime.now());
        }
    }
}
