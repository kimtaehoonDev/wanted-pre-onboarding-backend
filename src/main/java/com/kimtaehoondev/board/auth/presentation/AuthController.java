package com.kimtaehoondev.board.auth.presentation;

import com.kimtaehoondev.board.auth.application.AuthService;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(SignUpRequestDto dto) {
        Long savedId = authService.signUp(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/api/members/" + savedId)
            .build()
            .toUri();

        return ResponseEntity.created(location).build();
    }
}
