package com.theagilemonkeys.crmservice.service.user.request;

import lombok.Builder;

@Builder
public record UpdateUserRequest(String name, String surname, String imageUrl) {
}
