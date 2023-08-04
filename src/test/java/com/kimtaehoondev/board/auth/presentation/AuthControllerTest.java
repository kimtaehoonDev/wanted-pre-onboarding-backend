package com.kimtaehoondev.board.auth.presentation;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimtaehoondev.board.auth.application.AuthService;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @MockBean
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("회원가입 성공")
    void signupTest() throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto("k@naver.com", "123");
        Long savedId = 10L;

        when(authService.signUp(any(SignUpRequestDto.class)))
            .thenReturn(savedId);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/api/members/" + savedId)));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패 - 이메일 중복")
    void duplicatedEmail() throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto("k@naver.com", "123");

        doThrow(EmailDuplicatedException.class)
            .when(authService).signUp(any(SignUpRequestDto.class));

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }
}