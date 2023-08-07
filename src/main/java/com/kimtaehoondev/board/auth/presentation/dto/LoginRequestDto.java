package com.kimtaehoondev.board.auth.presentation.dto;

import static com.kimtaehoondev.board.auth.presentation.AuthController.EMAIL_CONDITION_REGEX;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {
    @Pattern(regexp = EMAIL_CONDITION_REGEX, message = "이메일이 형식에 맞지 않습니다.")
    @NotNull
    private final String email;

    @Size(min = 8)
    @NotNull
    private final String pwd;
}
