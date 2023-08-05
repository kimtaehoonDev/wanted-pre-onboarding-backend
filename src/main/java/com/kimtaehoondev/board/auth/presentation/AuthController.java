package com.kimtaehoondev.board.auth.presentation;

import com.kimtaehoondev.board.auth.application.AuthService;
import com.kimtaehoondev.board.auth.domain.TokenInfo;
import com.kimtaehoondev.board.auth.presentation.dto.LoginRequestDto;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import com.kimtaehoondev.board.exception.LoginInfoIncorrectException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    /**
     * 이메일에는 최소 한개의 @가 들어가야 합니다.
      */
    public static final String EMAIL_CONDITION_REGEX = "^.*@.*$";

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody @Validated SignUpRequestDto dto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getBindingErrors(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Long savedId = authService.signUp(dto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/api/members/" + savedId)
                .build()
                .toUri();

            return ResponseEntity.created(location).body(savedId);
        } catch (EmailDuplicatedException e) {
            Map<String, String> errors = getBindingError(e);
            return ResponseEntity.badRequest().body(errors);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Validated LoginRequestDto dto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getBindingErrors(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            TokenInfo token = authService.login(dto.getEmail(), dto.getPwd());
            return ResponseEntity.ok(token);
        } catch (LoginInfoIncorrectException e) {
            Map<String, String> errors = getBindingError(e);
            return ResponseEntity.badRequest().body(errors);
        }
    }

    private Map<String, String> getBindingErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    private Map<String, String> getBindingError(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("common", e.getMessage());
        return errors;
    }

}
