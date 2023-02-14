package com.theagilemonkeys.crmservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public record ApplicationProperties(DefaultUser defaultUser) {
    record DefaultUser(String email, String password) {
    }
}
