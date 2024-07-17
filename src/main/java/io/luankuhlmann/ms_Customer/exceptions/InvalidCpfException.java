package io.luankuhlmann.ms_Customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCpfException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCpfException(String ex) {
        super(ex);
    }
}
