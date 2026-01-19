package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.domain.Plano;
import dev.imrob.painelconsulta.dto.response.PlanoResponse;
import dev.imrob.painelconsulta.exception.BusinessException;
import dev.imrob.painelconsulta.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    public List<PlanoResponse> listarPlanosAtivos() {
        return planoRepository.findByActiveTrueOrderByPrecoAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PlanoResponse> listarTodosPlanos() {
        return planoRepository.findAllByOrderByPrecoAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PlanoResponse buscarPorId(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));
        return toResponse(plano);
    }

    @Transactional
    public PlanoResponse criarPlano(String nome, String descricao, BigDecimal preco,
                                    Integer limiteConsultas, Integer validadeDias) {
        Plano plano = Plano.builder()
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .limiteConsultas(limiteConsultas)
                .validadeDias(validadeDias)
                .active(true)
                .build();

        plano = planoRepository.save(plano);
        return toResponse(plano);
    }

    @Transactional
    public PlanoResponse atualizarPlano(Long id, String nome, String descricao, BigDecimal preco,
                                        Integer limiteConsultas, Integer validadeDias, boolean active) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        plano.setNome(nome);
        plano.setDescricao(descricao);
        plano.setPreco(preco);
        plano.setLimiteConsultas(limiteConsultas);
        plano.setValidadeDias(validadeDias);
        plano.setActive(active);

        plano = planoRepository.save(plano);
        return toResponse(plano);
    }

    @Transactional
    public void desativarPlano(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Plano não encontrado"));

        plano.setActive(false);
        planoRepository.save(plano);
    }

    private PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getDescricao(),
                plano.getPreco(),
                plano.getLimiteConsultas(),
                plano.getValidadeDias(),
                plano.isActive()
        );
    }
}
