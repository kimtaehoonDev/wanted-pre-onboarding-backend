package com.kimtaehoondev.board.exception.impl;

import com.kimtaehoondev.board.exception.CustomException;
import com.kimtaehoondev.board.exception.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
