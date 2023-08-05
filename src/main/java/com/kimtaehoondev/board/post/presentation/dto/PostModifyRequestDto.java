package com.kimtaehoondev.board.post.presentation.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostModifyRequestDto {
    @NotBlank
    private final String title;

    @NotBlank
    private final String contents;
}
