package dev.imrob.painelconsulta.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
    @NotNull(message = "O ID é obrigatório")
    Long id,

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    String telefone,

    String role,

    Boolean active,

    Long planoId
) {}
