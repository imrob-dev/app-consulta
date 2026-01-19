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
@Table("configuracoes")
public class Configuracao {

    @Id
    private Long id;
    private String chave;
    private String valor;
    private String descricao;
    private LocalDateTime updatedAt;
}
