package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.domain.Configuracao;
import dev.imrob.painelconsulta.repository.ConfiguracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    public static final String USAR_MOCK_CONSULTAS = "usar_mock_consultas";

    private final ConfiguracaoRepository configuracaoRepository;

    public boolean isUsarMockConsultas() {
        return configuracaoRepository.findByChave(USAR_MOCK_CONSULTAS)
                .map(config -> "true".equalsIgnoreCase(config.getValor()))
                .orElse(true); // Por padrão usa mock
    }

    @Transactional
    public void setUsarMockConsultas(boolean usarMock) {
        Configuracao config = configuracaoRepository.findByChave(USAR_MOCK_CONSULTAS)
                .orElse(Configuracao.builder()
                        .chave(USAR_MOCK_CONSULTAS)
                        .descricao("Define se as consultas usarão dados fictícios (mock) ou API externa real")
                        .build());

        config.setValor(String.valueOf(usarMock));
        config.setUpdatedAt(LocalDateTime.now());
        configuracaoRepository.save(config);
    }

    public String getConfiguracao(String chave, String valorPadrao) {
        return configuracaoRepository.findByChave(chave)
                .map(Configuracao::getValor)
                .orElse(valorPadrao);
    }

    @Transactional
    public void setConfiguracao(String chave, String valor, String descricao) {
        Configuracao config = configuracaoRepository.findByChave(chave)
                .orElse(Configuracao.builder()
                        .chave(chave)
                        .descricao(descricao)
                        .build());

        config.setValor(valor);
        config.setUpdatedAt(LocalDateTime.now());
        configuracaoRepository.save(config);
    }

    public List<Configuracao> listarConfiguracoes() {
        return (List<Configuracao>) configuracaoRepository.findAll();
    }
}
