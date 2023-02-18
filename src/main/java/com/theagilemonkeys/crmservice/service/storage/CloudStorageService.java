package com.theagilemonkeys.crmservice.service.storage;


import com.theagilemonkeys.crmservice.service.storage.request.UploadObjectRequest;

import java.net.URL;

public interface CloudStorageService {
    /**
     * Uploads the object with the requested data
     * @param newObjectRequest the object with the required parameters
     * @return the url of the cloud stored object
     */
    URL uploadObject(UploadObjectRequest newObjectRequest);

    /**
     * Removes the object from the cloud
     * @param objectKey the key of the object to delete
     */
    void removeObject(String objectKey);
}
