package com.example.demo.controller.config;

import com.example.demo.service.exception.NoteNotFoundException;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Map<String, List<String>>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> result = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> {
                    if (result.containsKey(e.getField())) {
                        result.get(e.getField()).add(e.getDefaultMessage());
                    } else {
                        result.put(e.getField(), asList(e.getDefaultMessage()));
                    }
                });
        return new ResponseEntity<>(getErrorsMap(result), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            NoteNotFoundException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, List<String>>> noteNotFoundException(Exception ex) {
        return getErrorsMap(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FileUploadException.class})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ResponseEntity<Map<String, List<String>>> fileUploadException(FileUploadException ex) {
        return getErrorsMap(ex, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(value = {
            UserAlreadyExistException.class,
            UserIncorrectPasswordException.class
    })
    public ResponseEntity<Map<String, List<String>>> conflictException(Exception ex) {
        return getErrorsMap(ex, HttpStatus.CONFLICT);
    }

    /* Helpers */

    private Map<String, Map<String, List<String>>> getErrorsMap(Map<String, List<String>> errors) {
        Map<String, Map<String, List<String>>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    private ResponseEntity<Map<String, List<String>>> getErrorsMap(Throwable ex, HttpStatus status) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), status);
    }

}
