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
@Table("users")
public class User {

    @Id
    private Long id;
    private String email;
    private String password;
    private String nome;
    private String telefone;
    private String role;
    private boolean active;
    private Long planoId;
    private LocalDateTime dataExpiracao;
    private Integer consultasUsadas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
