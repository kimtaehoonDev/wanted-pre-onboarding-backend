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
import com.kimtaehoondev.board.exception.impl.EmailDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @ValueSource(strings = {"k@naver.com","@","@@@@", "@가@" ,"@rk", ".@"})
    @WithMockUser
    @DisplayName("회원가입 성공 - 이메일에 @가 있는 경우")
    void hasGolbaengiInEmail(String email) throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto(email, "12345678");
        Long savedId = 10L;

        when(authService.signUp(any(SignUpRequestDto.class)))
            .thenReturn(savedId);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/api/members/" + savedId)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "        ", "@@@@@@@@", "dkalsjfaklhfaklhflakhfla"})
    @WithMockUser
    @DisplayName("회원가입 성공 - 비밀번호가 8자리 이상인 경우")
    void successPasswordLong(String pwd) throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto("k@naver.com", pwd);
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

    @ParameterizedTest
    @ValueSource(strings = {"naver.com","d","com"})
    @WithMockUser
    @DisplayName("회원가입 실패 - 이메일에 @이 없을 때")
    void noGolbaengiInEmail(String email) throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto(email, "123");

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","11","123","       ","1234567", " 12345 "})
    @WithMockUser
    @DisplayName("회원가입 실패 - 비밀번호의 길이가 8보다 짧을 때")
    void passwordShort(String pwd) throws Exception {
        SignUpRequestDto dto = new SignUpRequestDto("k@naver.com", pwd);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))).
            andExpect(status().isBadRequest());
    }

}