package com.pragma.powerup.infrastructure.exceptionhandler;

import com.mongodb.MongoException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReportControllerAdvisor {
    @ExceptionHandler({DataAccessException.class, MongoException.class})
    public ResponseEntity<Void> persistenceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
