package com.theagilemonkeys.crmservice.service.storage.execption;

public class ObjectNotFoundException extends CloudStorageException {

    public ObjectNotFoundException(String objectPath) {
        super("Object " + objectPath + " not found in cloud storage");
    }
}
