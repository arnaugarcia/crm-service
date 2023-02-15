package com.theagilemonkeys.crmservice.web.rest.errors;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ErrorResponse {
    int code;
    String message;

    List<FieldError> fieldErrors;
}
