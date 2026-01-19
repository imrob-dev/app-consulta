package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends CrudRepository<Consulta, Long>, PagingAndSortingRepository<Consulta, Long> {

    Page<Consulta> findByUserId(Long userId, Pageable pageable);

    List<Consulta> findByUserIdOrderByDataConsultaDesc(Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndDataConsultaGreaterThanEqual(Long userId, LocalDateTime startDate);

    Page<Consulta> findByUserIdAndTipo(Long userId, String tipo, Pageable pageable);
}
