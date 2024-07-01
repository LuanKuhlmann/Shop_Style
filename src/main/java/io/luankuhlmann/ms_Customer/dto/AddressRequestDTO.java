package io.luankuhlmann.ms_Customer.dto;

import io.luankuhlmann.ms_Customer.enums.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record AddressRequestDTO(
        @NotBlank State state,
        @NotBlank String city,
        @NotBlank String district,
        @NotBlank String street,
        @NotBlank String number,
        @NotBlank String cep,
        String complement,
        @NotNull Long customerId
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
