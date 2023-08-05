package com.kimtaehoondev.board.post.application.dto.request;

import lombok.Getter;

@Getter
public class PostModifyServiceRequestDto {
    private final Long postId;
    private final String title;
    private final String contents;
    private final String email;

    public PostModifyServiceRequestDto(Long postId, String title, String contents, String email) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.email = email;
    }
}
