package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.post.application.dto.PostWriteServiceRequestDto;

public interface PostService {
    Long writePost(PostWriteServiceRequestDto dto);
}
