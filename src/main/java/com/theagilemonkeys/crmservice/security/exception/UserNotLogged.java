package com.theagilemonkeys.crmservice.security.exception;

public class UserNotLogged extends RuntimeException {

    public UserNotLogged() {
        super("User not logged");
    }
}
