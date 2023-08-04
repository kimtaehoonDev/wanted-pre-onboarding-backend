package com.kimtaehoondev.board.auth.presentation.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequestDto {
    @Pattern(regexp = "^.*@.*$", message = "이메일이 형식에 맞지 않습니다.")
    private final String email;

    private final String pwd;
}
