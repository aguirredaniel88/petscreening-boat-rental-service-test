package com.petscreening.boatrental.exception;

import jakarta.validation.ValidationException;

public class PetNotFoundException extends ValidationException {
    public PetNotFoundException(String message) {
        super(message);
    }
}
