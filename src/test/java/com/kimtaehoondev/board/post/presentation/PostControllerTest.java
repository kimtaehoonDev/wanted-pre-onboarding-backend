package com.kimtaehoondev.board.post.presentation;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimtaehoondev.board.exception.MemberNotFoundException;
import com.kimtaehoondev.board.post.application.PostService;
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

@WebMvcTest(PostController.class)
class PostControllerTest {
    @MockBean
    PostService postService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // 게시물 생성
    @ParameterizedTest
    @ValueSource(strings = {"title", "@", " 1 ", "sakljfhajkshfajkfhjakhfjk"})
    @DisplayName("게시물 생성 성공 - 제목이 있는 경우")
    @WithMockUser
    void writePostHavingTitle(String title) throws Exception {
        Long writerId = 1229L;
        Long savedId = 123L;
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto(title, "contents", writerId);

        when(postService.writePost(any(PostWriteServiceRequestDto.class))).thenReturn(savedId);

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/api/posts/" + savedId)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"contents", "@", " 1 ", "sakljfhajkshfajkfhjakhfjk"})
    @DisplayName("게시물 생성 성공 - 내용이 있는 경우")
    @WithMockUser
    void writePostHavingContents(String contents) throws Exception {
        Long writerId = 1229L;
        Long savedId = 123L;
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto("title", contents, writerId);

        when(postService.writePost(any(PostWriteServiceRequestDto.class))).thenReturn(savedId);

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/api/posts/" + savedId)));
    }

    @Test
    @DisplayName("로그인되지 않은 사용자가 게시물을 작성할 때 401 상태를 반환한다")
    @WithMockUser
    void cantWriteUnauthenticatedMember() throws Exception {
        Long writerId = 1229L;
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto("title", "contents", writerId);
        doThrow(MemberNotFoundException.class)
            .when(postService).writePost(any(PostWriteServiceRequestDto.class));

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @WithMockUser
    @DisplayName("제목이 비어있으면, 게시물 등록에 실패한다")
    void noTitle(String title) throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto(title, "contents");

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @WithMockUser
    @DisplayName("내용이 비어있으면, 게시물 등록에 실패한다")
    void noContents(String contents) throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto("title", contents);

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    // 게시물 페이징

    // 게시물 단건 읽기

    // 게시물 수정

    // 게시물 삭제


}