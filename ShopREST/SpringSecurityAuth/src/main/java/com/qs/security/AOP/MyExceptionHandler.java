package com.qs.security.AOP;

import com.qs.security.domain.response.ExceptionResponse;
import com.qs.security.exception.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity handleInvalidCredentialsException(InvalidCredentialsException e){
        System.out.println(e.getMessage());
        return new ResponseEntity(ExceptionResponse.builder()
                .message("Incorrect credentials, please try again.")
                .build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleException(Exception e){
        e.printStackTrace();
        return new ResponseEntity(ExceptionResponse.builder()
                .message("Bad Request.")
                .build(), HttpStatus.OK);
    }
}