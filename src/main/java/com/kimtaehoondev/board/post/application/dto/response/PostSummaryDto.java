package com.kimtaehoondev.board.post.application.dto.response;

import java.time.LocalDateTime;

public interface PostSummaryDto {
    Long getId();

    String getTitle();

    String getWriterEmail();

    LocalDateTime getUpdatedAt();
}
