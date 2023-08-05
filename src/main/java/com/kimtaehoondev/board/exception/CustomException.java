package com.kimtaehoondev.board.exception;

public class CustomException extends RuntimeException {
    public CustomException(ErrorCode code) {
        super(code.getMessage());
    }
}
