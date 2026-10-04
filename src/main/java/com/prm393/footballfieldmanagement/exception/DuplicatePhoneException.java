package com.prm393.footballfieldmanagement.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException() {
        super("Phone is already in use");
    }
}
