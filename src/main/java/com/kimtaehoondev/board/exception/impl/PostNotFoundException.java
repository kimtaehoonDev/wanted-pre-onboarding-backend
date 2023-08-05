package com.kimtaehoondev.board.exception.impl;

import com.kimtaehoondev.board.exception.CustomException;
import com.kimtaehoondev.board.exception.ErrorCode;

public class PostNotFoundException extends CustomException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
