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
@Table("consultas")
public class Consulta {

    @Id
    private Long id;
    private Long userId;
    private String tipo;
    private String valor;
    private String resultado;
    private LocalDateTime dataConsulta;
    private String status;
}
