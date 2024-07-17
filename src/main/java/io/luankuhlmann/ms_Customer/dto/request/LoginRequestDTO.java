package io.luankuhlmann.ms_Customer.dto.request;

import java.io.Serial;
import java.io.Serializable;

public record LoginRequestDTO (
        String email,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}