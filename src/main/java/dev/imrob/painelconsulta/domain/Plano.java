package dev.imrob.painelconsulta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("planos")
public class Plano {

    @Id
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer limiteConsultas;
    private Integer validadeDias;
    private boolean active;
}
