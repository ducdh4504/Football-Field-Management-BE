package com.prm393.footballfieldmanagement.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("Email is already in use");
    }
}
