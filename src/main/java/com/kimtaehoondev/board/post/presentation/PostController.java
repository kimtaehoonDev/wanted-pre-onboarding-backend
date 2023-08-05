package com.kimtaehoondev.board.post.presentation;

import com.kimtaehoondev.board.post.application.PostService;
import com.kimtaehoondev.board.post.application.dto.PostWriteServiceRequestDto;
import com.kimtaehoondev.board.post.application.dto.response.PostDetailDto;
import com.kimtaehoondev.board.post.application.dto.response.PostSummaryDto;
import com.kimtaehoondev.board.post.presentation.dto.PostWriteRequestDto;
import com.kimtaehoondev.board.post.presentation.pageable.PageRequestFactory;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    public static final Long memberId = 123L; // TODO 로그인 구현 후 변경

    private final PostService postService;
    private final PageRequestFactory pageRequestFactory;

    @PostMapping
    public ResponseEntity<?> writePost(@RequestBody @Validated PostWriteRequestDto dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        PostWriteServiceRequestDto serviceDto =
            new PostWriteServiceRequestDto(dto.getTitle(), dto.getContents(), memberId);
        Long savedId = postService.writePost(serviceDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/api/posts/" + savedId)
            .build()
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<?> getPostsByPage(@RequestParam(required = false) Integer page) {
        Pageable pageable = pageRequestFactory.make(page);
        List<PostSummaryDto> posts = postService.getPostsByPage(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable(name = "id") Long postId) {
        PostDetailDto post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();

    }

}
