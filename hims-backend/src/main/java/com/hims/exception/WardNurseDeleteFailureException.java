package com.hims.exception;

public class WardNurseDeleteFailureException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public WardNurseDeleteFailureException() {
        super("Fail to delete ward nurse! Some patient is still hospitalized.");
    }
}
