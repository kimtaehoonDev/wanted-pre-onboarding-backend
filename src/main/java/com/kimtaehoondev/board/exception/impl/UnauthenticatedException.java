package com.kimtaehoondev.board.exception.impl;

import com.kimtaehoondev.board.exception.CustomException;
import com.kimtaehoondev.board.exception.ErrorCode;

public class UnauthenticatedException extends CustomException {
    public UnauthenticatedException() {
        super(ErrorCode.UNAUTHENTICATED);
    }
}
