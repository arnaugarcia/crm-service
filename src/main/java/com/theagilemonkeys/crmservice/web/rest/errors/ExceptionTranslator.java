package com.theagilemonkeys.crmservice.web.rest.errors;

import com.theagilemonkeys.crmservice.service.user.execption.ImmutableUser;
import com.theagilemonkeys.crmservice.service.user.execption.OperationNotAllowed;
import com.theagilemonkeys.crmservice.service.user.execption.UserAlreadyExists;
import com.theagilemonkeys.crmservice.service.user.execption.UserNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

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
     * Handle OperationNotAllowed exception and return a 403 Forbidden
     * @param ex the exception to handle
     * @return the 403 Forbidden response
     */
    @ExceptionHandler(OperationNotAllowed.class)
    public ResponseEntity<ErrorResponse> handleOperationNotAllowedException(OperationNotAllowed ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(FORBIDDEN.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, FORBIDDEN);
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
                .collect(Collectors.toList());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("Validation failed")
                .fieldErrors(fieldErrors)
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}
