package com.qs.shop.AOP;

import com.qs.shop.exception.InvalidCredentialsException;
import com.qs.shop.exception.NotEnoughInventoryException;
import com.qs.shop.exception.PriceMissMatchException;
import com.qs.shop.exception.ProductNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.qs.shop.domain.response.ExceptionResponse;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity handleInvalidException(InvalidCredentialsException e){
        return new ResponseEntity(
                ExceptionResponse.builder()
                        .message("Incorrect credentials, please try again.")
                        .status("fail")
                        .build(),
                HttpStatus.OK);
    }

    @ExceptionHandler(value = {ProductNotExistException.class})
    public ResponseEntity handleProductNotExistException(ProductNotExistException e){
        return new ResponseEntity(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .status("fail")
                        .build(),
                HttpStatus.OK);
    }

    @ExceptionHandler(value = {PriceMissMatchException.class})
    public ResponseEntity handleException(PriceMissMatchException e){
        return new ResponseEntity(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .status("fail")
                        .build()
                , HttpStatus.OK);
    }

    @ExceptionHandler(value = NotEnoughInventoryException.class)
    public ResponseEntity handleNotEnoughException(NotEnoughInventoryException e){
        return new ResponseEntity(
                ExceptionResponse.builder()
                        .message("Product quantity is not enough.")
                        .status("fail")
                        .build()
                , HttpStatus.OK);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity unexpectedException(Exception e){
        e.printStackTrace();
        return new ResponseEntity(
                ExceptionResponse.builder()
                        .message("Unexpected exception occurs!")
                        .status("fail")
                        .build()
                , HttpStatus.OK);
    }
}
