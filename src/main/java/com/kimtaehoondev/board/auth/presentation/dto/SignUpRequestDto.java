package com.kimtaehoondev.board.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequestDto {
    private final String email;
    private final String pwd;
}
