package com.example.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoCustomersFoundException.class})
    public ResponseEntity<ApiError> handleNoCustomersFoundOrExist(NoCustomersFoundException e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiError apiError = new ApiError(
                e.getMessage(),
                e,
                httpStatus,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ApiError> handleDataCustomersAlreadyExist(IllegalStateException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiError apiError = new ApiError(
                e.getMessage(),
                e,
                httpStatus,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, httpStatus);
    }


}
