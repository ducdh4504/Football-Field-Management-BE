package com.prm393.footballfieldmanagement.exception;

public class CurrentUserNotFoundException extends RuntimeException {

    public CurrentUserNotFoundException() {
        super("Current user was not found");
    }
}
