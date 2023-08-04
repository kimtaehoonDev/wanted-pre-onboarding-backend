package com.kimtaehoondev.board.post.presentation;

import com.kimtaehoondev.board.post.application.PostService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    public static final Long memberId = 123L; // TODO 로그인 구현 후 변경

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> writePost(PostWriteRequestDto dto) {
        PostWriteServiceRequestDto serviceDto =
            new PostWriteServiceRequestDto(dto.getTitle(), dto.getContents(), memberId);
        Long savedId = postService.writePost(serviceDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/api/posts/" + savedId)
            .build()
            .toUri();
        return ResponseEntity.created(location).build();
    }
}
