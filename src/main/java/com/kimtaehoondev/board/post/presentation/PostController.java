package com.kimtaehoondev.board.post.presentation;

import com.kimtaehoondev.board.exception.MemberNotFoundException;
import com.kimtaehoondev.board.post.application.PostService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> writePost(@RequestBody @Validated PostWriteRequestDto dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            PostWriteServiceRequestDto serviceDto =
                new PostWriteServiceRequestDto(dto.getTitle(), dto.getContents(), memberId);
            Long savedId = postService.writePost(serviceDto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/api/posts/" + savedId)
                .build()
                .toUri();
            return ResponseEntity.created(location).build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
