package com.theagilemonkeys.crmservice.web.rest.errors;

import com.theagilemonkeys.crmservice.service.customer.exception.CustomerNotFound;
import com.theagilemonkeys.crmservice.service.storage.execption.ObjectNotFoundException;
import com.theagilemonkeys.crmservice.service.user.execption.ImmutableUser;
import com.theagilemonkeys.crmservice.service.user.execption.UserAlreadyExists;
import com.theagilemonkeys.crmservice.service.user.execption.UserNotFound;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    /**
     * Handle UserAlreadyExists exception and return a 400 Bad Request
     * @param ex the exception to handle
     * @return the 400 Bad Request response
     */
    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExists ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    /**
     * Handle ImmutableUser exception and return a 403 Forbidden
     * @param ex the exception to handle
     * @return the 403 Forbidden response
     */
    @ExceptionHandler(ImmutableUser.class)
    public ResponseEntity<ErrorResponse> handleUserImmutableExistsException(ImmutableUser ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(FORBIDDEN.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    /**
     * Handle UserNotFound exception and return a 404 Not Found
     * @param ex the exception to handle
     * @return the 404 Not Found response
     */
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFound ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    /**
     * Handle HttpMessageNotReadableException and return a 400 Bad Request
     * @param ex the exception to handle
     * @return the 400 Bad Request response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("Malformed JSON request for field: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    /**
     * Handle PropertyReferenceException and return a 400 Bad Request
     * @param ex the exception to handle
     * @return the 400 Bad Request response
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("Invalid property for sorting: " + ex.getPropertyName())
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    /**
     * Handle CustomerNotFound exception and return a 400 Bad Request
     * @param ex the exception to handle
     * @return the 400 Bad Request response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(constraintViolation -> FieldError.builder()
                        .field(constraintViolation.getPropertyPath().toString())
                        .message(constraintViolation.getMessage())
                        .build())
                .collect(toList());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("Validation failed")
                .fieldErrors(fieldErrors)
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    /**
     * Handle MethodArgumentNotValidException and return a 400 Bad Request with the list of field errors
     * @param ex the exception to handle
     * @return the 400 Bad Request response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(toList());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("Validation failed")
                .fieldErrors(fieldErrors)
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    /**
     * Handle S3Exception and return a 500 Internal Server Error
     * @param ex the exception to handle
     * @return the 500 Internal Server Error response
     */
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle CustomerNotFound exception and return a 404 Not Found
     * @param ex the exception to handle
     * @return the 404 Not Found response
     */
    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFound ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    /**
     * Handle ObjectNotFoundException exception and return a 404 Not Found
     * @param ex the exception to handle
     * @return the 404 Not Found response
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }
}
