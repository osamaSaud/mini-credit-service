package com.credit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends ResourceAlreadyExistsException {
    public DuplicateEmailException(String email) {
        super("Customer", "email", email);
    }
} 