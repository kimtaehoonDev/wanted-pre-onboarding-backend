package com.kimtaehoondev.board.auth.application;

import com.kimtaehoondev.board.auth.domain.TokenInfo;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;

public interface AuthService {
    Long signUp(SignUpRequestDto dto);

    TokenInfo login(String memberId, String password);
}
