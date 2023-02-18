package com.theagilemonkeys.crmservice.service.user.execption;

public class OperationNotAllowed extends RuntimeException {

    public OperationNotAllowed() {
        super("Operation not allowed");
    }
}
