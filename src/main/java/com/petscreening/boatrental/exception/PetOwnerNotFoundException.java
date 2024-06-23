package com.petscreening.boatrental.exception;

import jakarta.validation.ValidationException;

public class PetOwnerNotFoundException extends ValidationException {

    public PetOwnerNotFoundException(String message) {
        super(message);
    }
}
