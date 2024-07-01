package io.luankuhlmann.ms_Customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String ex) {
        super(ex);
    }
}