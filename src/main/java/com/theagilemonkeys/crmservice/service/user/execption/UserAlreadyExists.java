package com.theagilemonkeys.crmservice.service.user.execption;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists() {
        super("User email already exists");
    }
}
