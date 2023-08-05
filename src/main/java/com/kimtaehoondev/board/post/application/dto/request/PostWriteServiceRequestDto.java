package com.kimtaehoondev.board.post.application.dto.request;

import lombok.Getter;

@Getter
public class PostWriteServiceRequestDto {
    private final String title;
    private final String contents;
    private final String email;

    public PostWriteServiceRequestDto(String title, String contents, String email) {
        this.title = title;
        this.contents = contents;
        this.email = email;
    }
}
