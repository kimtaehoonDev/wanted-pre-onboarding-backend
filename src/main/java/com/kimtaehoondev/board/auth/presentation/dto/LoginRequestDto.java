package com.kimtaehoondev.board.auth.presentation.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {
    @Pattern(regexp = "^.*@.*$", message = "이메일이 형식에 맞지 않습니다.")
    private final String email;

    @Size(min = 8)
    private final String pwd;
}
