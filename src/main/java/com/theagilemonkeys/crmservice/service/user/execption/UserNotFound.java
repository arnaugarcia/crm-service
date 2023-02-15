package com.theagilemonkeys.crmservice.service.user.execption;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("User not found");
    }
}
