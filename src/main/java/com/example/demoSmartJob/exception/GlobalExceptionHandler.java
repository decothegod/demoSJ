package com.example.demoSmartJob.exception;

import com.example.demoSmartJob.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.demoSmartJob.util.Constants.INTERNAL_SERVER_ERROR_MSG;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceExceptionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(ServiceExceptionNotFound e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceExceptionBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(ServiceExceptionBadRequest e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceExceptionUnauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleException(ServiceExceptionUnauthorized e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(INTERNAL_SERVER_ERROR_MSG)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}