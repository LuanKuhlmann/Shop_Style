package io.luankuhlmann.ms_Customer.domain.dto.request;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public record CustomerRequestDTO(
        @NotBlank @Size(min = 3) String firstName,
        @NotBlank @Size(min = 3) String lastName,
        @NotBlank @Pattern(regexp = "Masculino|Feminino") String sex,
        @NotBlank @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") String cpf,
        @NotNull @Past LocalDate birthdate,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password,
        boolean active
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}