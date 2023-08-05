package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.post.application.dto.PostWriteServiceRequestDto;
import com.kimtaehoondev.board.post.application.dto.response.PostDetailDto;
import com.kimtaehoondev.board.post.application.dto.response.PostSummaryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long writePost(PostWriteServiceRequestDto dto);

    List<PostSummaryDto> getPostsByPage(Pageable pageable);

    PostDetailDto getPost(Long postId);

    Long deletePost(Long postId, String email);
}
