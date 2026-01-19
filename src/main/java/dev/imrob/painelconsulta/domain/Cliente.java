package dev.imrob.painelconsulta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("clientes")
public class Cliente {

    @Id
    private Long id;
    private String nome;
    private String documento;
    private String email;
    private String telefone;
    private boolean active;
    private LocalDateTime createdAt;
}
