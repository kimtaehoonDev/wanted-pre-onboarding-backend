package com.kimtaehoondev.board.member.application.dto;

import java.time.LocalDateTime;

public interface MemberInfo {
    Long getId();

    String getEmail();

    LocalDateTime getUpdatedAt();
}
