package com.theagilemonkeys.crmservice.service.user.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserDTO(String name, String surname, String email, String imageUrl, String[] roles) implements Serializable {
}
