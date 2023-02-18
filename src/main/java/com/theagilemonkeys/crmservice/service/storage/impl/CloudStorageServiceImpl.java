package com.theagilemonkeys.crmservice.service.storage.impl;

import com.theagilemonkeys.crmservice.config.AWSClientProperties;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import com.theagilemonkeys.crmservice.service.storage.execption.ObjectNotFoundException;
import com.theagilemonkeys.crmservice.service.storage.request.UploadObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;

import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;
import static software.amazon.awssdk.services.s3.model.PutObjectRequest.builder;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private final Logger log = LoggerFactory.getLogger(CloudStorageService.class);

    private final S3Client s3Client;
    private final AWSClientProperties awsClientProperties;

    public CloudStorageServiceImpl(S3Client s3Client, AWSClientProperties awsClientProperties) {
        this.s3Client = s3Client;
        this.awsClientProperties = awsClientProperties;
    }

    @Override
    public URL uploadObject(UploadObjectRequest uploadObjectRequest) {
        log.debug("Uploading object to AmazonS3 with params {}", uploadObjectRequest);
        PutObjectRequest putObjectRequest = builder()
                .bucket(awsClientProperties.bucket())
                .key(uploadObjectRequest.getUploadPath())
                .build();

        s3Client.putObject(putObjectRequest, fromBytes(uploadObjectRequest.getData()));
        log.debug("Finished uploading object to AWS");
        return findFileUrl(uploadObjectRequest.getUploadPath());
    }

    @Override
    public void removeObject(String objectKey) {
        if (!existsObject(objectKey)) {
            throw new ObjectNotFoundException(objectKey);
        }
        final DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(awsClientProperties.bucket())
                .key(objectKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private URL findFileUrl(String path) {
        log.debug("Finding URL of the bucket by path {}", path);
        GetUrlRequest request = GetUrlRequest.builder().bucket(awsClientProperties.bucket()).key(path).build();
        return s3Client.utilities().getUrl(request);
    }

    private boolean existsObject(String path) {
        log.debug("Checking if the file exists in the bucket {}", path);
        GetObjectRequest request = GetObjectRequest.builder().bucket(awsClientProperties.bucket()).key(path).build();
        final GetObjectResponse response = s3Client.getObject(request).response();
        return response.hasMetadata();
    }
}
