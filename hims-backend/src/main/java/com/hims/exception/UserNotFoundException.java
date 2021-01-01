package com.hims.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public  UserNotFoundException(String id) {
        super("Id '" + id + "' not found");
    }
}
