package io.luankuhlmann.ms_Customer.dto.response;

import io.luankuhlmann.ms_Customer.models.enums.Gender;
import io.luankuhlmann.ms_Customer.models.Address;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        Gender gender,
        String cpf,
        LocalDate birthdate,
        String email,
        String password,
        boolean active,
        List<Address> addresses
) {}