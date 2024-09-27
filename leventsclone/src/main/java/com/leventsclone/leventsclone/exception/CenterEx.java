package com.leventsclone.leventsclone.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CenterEx {

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> getStatusEmailExist(AuthenticationException e) {
        System.out.println("tai khoan gap loi");
        return ResponseEntity.status(500).body("Email already is used");
    }
}
