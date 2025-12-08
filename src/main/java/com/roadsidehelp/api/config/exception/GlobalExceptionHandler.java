package com.roadsidehelp.api.config.exception;

import com.roadsidehelp.api.core.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorResponse resp = ErrorResponse.of(
                (ex.getErrorCode() == null ? ErrorCode.INTERNAL_ERROR : ex.getErrorCode()),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(ex.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : ex.getHttpStatus())
                .body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();

        ErrorResponse resp = ErrorResponse.of(ErrorCode.BAD_REQUEST, "Validation failed", String.join("; ", errors));
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));
        ErrorResponse resp = ErrorResponse.of(ErrorCode.BAD_REQUEST, "Constraint violation", details);
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        // In production log the exception stacktrace
        ErrorResponse resp = ErrorResponse.of(ErrorCode.INTERNAL_ERROR, "Something went wrong", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}
