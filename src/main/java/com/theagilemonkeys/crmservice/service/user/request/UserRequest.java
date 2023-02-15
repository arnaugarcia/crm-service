package com.theagilemonkeys.crmservice.service.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRequest(String name, String surname, @NotNull String email, @NotNull String password) {
}
