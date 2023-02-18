package com.theagilemonkeys.crmservice.service.storage.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class UploadRequest {

    byte[] data;
    String fileName;
    String destinationFolder;
    String fileExtension;
}
