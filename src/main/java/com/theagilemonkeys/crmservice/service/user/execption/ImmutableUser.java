package com.theagilemonkeys.crmservice.service.user.execption;

public class ImmutableUser extends RuntimeException {
    public ImmutableUser() {
        super("User is a super admin");
    }
}
