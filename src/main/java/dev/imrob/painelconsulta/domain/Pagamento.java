package dev.imrob.painelconsulta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pagamentos")
public class Pagamento {

    @Id
    private Long id;
    private Long userId;
    private Long planoId;
    private BigDecimal valor;
    private String codigoPix;
    private String status; // PENDENTE, CONFIRMADO, CANCELADO
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}
