package dev.imrob.painelconsulta.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "O telefone é obrigatório")
    String telefone,

    @NotBlank(message = "A senha é obrigatória")
    String password,

    Long planoId
) {}
