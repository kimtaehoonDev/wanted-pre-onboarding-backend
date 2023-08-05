package com.kimtaehoondev.board.exception.impl;

import com.kimtaehoondev.board.exception.CustomException;
import com.kimtaehoondev.board.exception.ErrorCode;

public class EmailDuplicatedException extends CustomException {
    public EmailDuplicatedException() {
        super(ErrorCode.EMAIL_DUPLICATED);
    }
}
