package com.theagilemonkeys.crmservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "application.aws", ignoreUnknownFields = false)
public record AWSClientProperties(String bucket, String region) {
}
