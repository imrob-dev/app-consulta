package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Pagamento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

    Optional<Pagamento> findByCodigoPix(String codigoPix);

    List<Pagamento> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Pagamento> findFirstByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}
