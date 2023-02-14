package com.theagilemonkeys.crmservice.service.user.request;

import lombok.Builder;

@Builder
public record UserRequest(String email, String password) {
}
