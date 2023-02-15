package com.theagilemonkeys.crmservice.web.rest.errors;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
public class FieldError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    public FieldError(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }
}
