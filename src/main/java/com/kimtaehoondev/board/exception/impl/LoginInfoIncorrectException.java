package com.kimtaehoondev.board.exception.impl;

import com.kimtaehoondev.board.exception.CustomException;
import com.kimtaehoondev.board.exception.ErrorCode;

public class LoginInfoIncorrectException extends CustomException {
    public LoginInfoIncorrectException() {
        super(ErrorCode.LOGIN_INFO_INCORRECT);
    }
}
