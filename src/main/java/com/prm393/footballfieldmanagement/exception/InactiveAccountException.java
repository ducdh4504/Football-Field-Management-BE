package com.prm393.footballfieldmanagement.exception;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException() {
        super("This account is inactive");
    }
}
