package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Plano;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoRepository extends CrudRepository<Plano, Long>, PagingAndSortingRepository<Plano, Long> {

    List<Plano> findByActiveTrueOrderByPrecoAsc();

    List<Plano> findAllByOrderByPrecoAsc();
}
