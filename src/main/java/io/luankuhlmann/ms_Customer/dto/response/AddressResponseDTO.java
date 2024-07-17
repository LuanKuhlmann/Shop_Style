package io.luankuhlmann.ms_Customer.dto.response;

import io.luankuhlmann.ms_Customer.models.enums.State;

public record AddressResponseDTO(
        Long id,
        State state,
        String city,
        String district,
        String street,
        String number,
        String cep,
        String complement,
        Long customerId
) {
}
