package com.kimtaehoondev.board.auth.application;

import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;

public interface AuthService {
    Long signUp(SignUpRequestDto dto);
}
