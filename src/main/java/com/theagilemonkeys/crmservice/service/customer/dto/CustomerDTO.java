package com.theagilemonkeys.crmservice.service.customer.dto;

import lombok.Builder;

@Builder
public record CustomerDTO(String id, String name, String surname, String photoUrl) {
}
