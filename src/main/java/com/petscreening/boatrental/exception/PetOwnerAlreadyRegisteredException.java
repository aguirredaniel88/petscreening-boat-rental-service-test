package com.petscreening.boatrental.exception;

import jakarta.validation.ValidationException;

public class PetOwnerAlreadyRegisteredException extends ValidationException {
    public PetOwnerAlreadyRegisteredException(String message) {
        super(message);
    }
}
