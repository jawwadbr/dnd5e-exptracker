package com.jawbr.dnd5e.exptracker.exception.handler;

import com.jawbr.dnd5e.exptracker.exception.ClassNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.errorResponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ClassExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ClassNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
