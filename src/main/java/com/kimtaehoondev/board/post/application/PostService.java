package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.post.presentation.PostWriteServiceRequestDto;

public interface PostService {
    Long writePost(PostWriteServiceRequestDto dto);
}
