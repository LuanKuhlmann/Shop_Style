package io.luankuhlmann.ms_Customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerAlreadyRegisteredException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CustomerAlreadyRegisteredException(String ex) {
        super(ex);
    }
}
