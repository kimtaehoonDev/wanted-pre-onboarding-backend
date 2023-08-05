package com.kimtaehoondev.board.post.application.dto.response;

public interface PostDetailDto {
    Long getId();

    String getTitle();

    String getContents();

    String getWriterEmail();
}
