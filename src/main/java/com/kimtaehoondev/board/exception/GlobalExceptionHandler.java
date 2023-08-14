package com.kimtaehoondev.board.exception;

import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.exception.impl.PostNotFoundException;
import com.kimtaehoondev.board.exception.impl.UnauthenticatedException;
import com.kimtaehoondev.board.exception.impl.UnauthorizedException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Void> postNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String, String>> memberNotFound(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("common", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> unauthorized(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("common", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Map<String, String>> unauthenticated(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("common", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

}
