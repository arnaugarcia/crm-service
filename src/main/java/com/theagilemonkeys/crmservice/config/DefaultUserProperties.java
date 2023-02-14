package com.theagilemonkeys.crmservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.default-user", ignoreUnknownFields = false)
public record DefaultUserProperties(String email, String password) { }

