package com.theagilemonkeys.crmservice.service.storage.execption;

public abstract class CloudStorageException extends RuntimeException {

    public CloudStorageException(String message) {
        super(message);
    }
}
