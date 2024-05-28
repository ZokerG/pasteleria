package com.example.pasteleria.ex;

public class ErrorMessages {

    private ErrorMessages() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ACCOUNT_NOT_MATCH = "error.identity.account.not-match";
    public static final String ACCOUNT_LOCKED = "error.identity.account.locked";
    public static final String EMAIL_ALREADY_EXISTS = "error.identity.account.disabled";
    public static final String PASSWORDS_DO_NOT_MATCH = "error.identity.account.not-found";
}
