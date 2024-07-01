package io.luankuhlmann.ms_Customer.dto;

import io.luankuhlmann.ms_Customer.models.Address;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public record CustomerResponseDTO(
        @NotBlank @Size(min = 3) String firstName,
        @NotBlank @Size(min = 3) String lastName,
        @NotBlank @Pattern(regexp = "Masculino|Feminino") String sex,
        @NotBlank @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") String cpf,
        @NotNull @Past Date birthdate,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password,
        boolean active,
        List<Address> addresses
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}