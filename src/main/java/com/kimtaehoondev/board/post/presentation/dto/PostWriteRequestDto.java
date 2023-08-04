package com.kimtaehoondev.board.post.presentation.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostWriteRequestDto {
    @NotBlank
    private final String title;

    @NotBlank
    private final String contents;

    public PostWriteRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
