package com.kimtaehoondev.board.post.presentation;

import lombok.Getter;

@Getter
public class PostWriteServiceRequestDto {
    private final String title;
    private final String contents;
    private final Long memberId;

    public PostWriteServiceRequestDto(String title, String contents, Long memberId) {
        this.title = title;
        this.contents = contents;
        this.memberId = memberId;
    }
}
