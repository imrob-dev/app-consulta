package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long>, PagingAndSortingRepository<Cliente, Long> {

    Optional<Cliente> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    Page<Cliente> findByActiveTrue(Pageable pageable);

    Page<Cliente> findByNomeContainingIgnoreCaseOrDocumentoContainingIgnoreCase(String nome, String documento, Pageable pageable);
}
