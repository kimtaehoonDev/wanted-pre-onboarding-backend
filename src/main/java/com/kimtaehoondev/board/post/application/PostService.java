package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.post.application.dto.PostWriteServiceRequestDto;
import com.kimtaehoondev.board.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long writePost(PostWriteServiceRequestDto dto);

    List<Post> getPostsByPage(Pageable pageable);
}
