package com.kimtaehoondev.board.post.application.dto.response;

import java.time.LocalDateTime;

public interface PostDetailDto {
    Long getId();

    String getTitle();

    String getContents();

    String getWriterEmail();

    LocalDateTime getUpdatedAt();
}
