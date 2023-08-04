package com.kimtaehoondev.board.post.presentation;

import lombok.Getter;

@Getter
public class PostWriteRequestDto {
    private final String title;
    private final String contents;

    public PostWriteRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
