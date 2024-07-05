package io.luankuhlmann.ms_Customer.dto.response;

import io.luankuhlmann.ms_Customer.models.enums.Sex;
import io.luankuhlmann.ms_Customer.models.Address;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        Sex sex,
        String cpf,
        LocalDate birthdate,
        String email,
        String password,
        boolean active,
        List<Address> addresses
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}