package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Configuracao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoRepository extends CrudRepository<Configuracao, Long> {

    Optional<Configuracao> findByChave(String chave);
}
