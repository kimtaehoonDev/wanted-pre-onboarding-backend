package com.kimtaehoondev.board.post.presentation.dto.response;

import com.kimtaehoondev.board.post.application.dto.response.PostDetailDto;
import com.kimtaehoondev.board.post.application.dto.response.PostSummaryDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostResponseDto {
    private Long id;

    private String title;

    private String contents;

    private String writerEmail;

    private LocalDateTime updatedAt;

    public static PostResponseDto from(PostSummaryDto dto) {
        return new PostResponseDto(dto.getId(), dto.getTitle(),
            null, dto.getWriterEmail(), dto.getUpdatedAt());
    }

    public static PostResponseDto from(PostDetailDto dto) {
        return new PostResponseDto(dto.getId(), dto.getTitle(),
            dto.getContents(), dto.getWriterEmail(), dto.getUpdatedAt());
    }
}
