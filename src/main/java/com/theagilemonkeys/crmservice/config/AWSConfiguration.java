package com.theagilemonkeys.crmservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static software.amazon.awssdk.regions.Region.of;
import static software.amazon.awssdk.services.s3.S3Client.builder;

@Configuration
public class AWSConfiguration {

    private final AWSClientProperties awsClientProperties;
    private final S3Client s3Client;

    public AWSConfiguration(AWSClientProperties awsClientProperties) {
        this.awsClientProperties = awsClientProperties;
        this.s3Client = createAWSS3Client();
    }

    @Bean
    public S3Client getAmazonS3Client() {
        return s3Client;
    }

    private S3Client createAWSS3Client() {
        final Region region = of(awsClientProperties.region());
        return builder().region(region).build();
    }
}
