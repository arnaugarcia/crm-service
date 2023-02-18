package com.theagilemonkeys.crmservice.service.customer.exception;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound() {
        super("Customer not found");
    }
}
