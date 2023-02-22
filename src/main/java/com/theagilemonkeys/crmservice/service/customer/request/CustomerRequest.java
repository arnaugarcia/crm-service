package com.theagilemonkeys.crmservice.service.customer.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CustomerRequest(@NotNull String name, @NotNull String surname, CustomerPhotoRequest photo) {
    public record CustomerPhotoRequest(byte[] data, PhotoFormat photoContentType) {}
}
