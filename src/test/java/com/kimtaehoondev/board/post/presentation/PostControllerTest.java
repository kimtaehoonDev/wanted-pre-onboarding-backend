package com.kimtaehoondev.board.post.presentation;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.exception.impl.PostNotFoundException;
import com.kimtaehoondev.board.exception.impl.UnauthorizedException;
import com.kimtaehoondev.board.post.application.PostService;
import com.kimtaehoondev.board.post.application.dto.request.PostModifyServiceRequestDto;
import com.kimtaehoondev.board.post.application.dto.request.PostWriteServiceRequestDto;
import com.kimtaehoondev.board.post.application.dto.response.PostDetailDto;
import com.kimtaehoondev.board.post.presentation.dto.PostModifyRequestDto;
import com.kimtaehoondev.board.post.presentation.dto.PostWriteRequestDto;
import com.kimtaehoondev.board.post.presentation.pageable.PageRequestFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {
    public static final int DEFAULT_SIZE = 5;

    @MockBean
    PostService postService;

    @MockBean
    PageRequestFactory pageRequestFactory;

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
        String email = "emai@naver.com";
        Long savedId = 123L;
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto(title, "contents", email);

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
        String email = "emai@naver.com";
        Long savedId = 123L;
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto("title", contents, email);

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
        String email = "emai@naver.com";
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto("title", "contents", email);
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
    @Test
    @WithMockUser
    @DisplayName("페이지 정보를 입력해 게시물들을 가져온다")
    void paging() throws Exception {
        //given
        // 페이지를 만드는 공장에서 무조건 pageable 인스턴스를 반환하게 한다
        int page = 2;
        PageRequest pageable = PageRequest.of(page, DEFAULT_SIZE);
        when(pageRequestFactory.make(page)).thenReturn(pageable);
        when(postService.getPostsByPage(pageable)).thenReturn(List.of());


        mockMvc.perform(get("/api/posts?page=" + page).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray()); // 최상단이 배열임을 확인한다
    }

    // 게시물 단건 읽기
    @Test
    @WithMockUser
    @DisplayName("단건 게시물 조회에 성공한다")
    void searchAlone() throws Exception {
        Long postId = 1L;

        PostDetailDto dto = makePostDetailDto();
        when(postService.getPost(postId)).thenReturn(dto);
        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("게시물이 없으면 단건 게시물 조회에 실패한다")
    void searchAloneFail() throws Exception {
        long postId = 1L;
        doThrow(PostNotFoundException.class)
            .when(postService).getPost(postId);
        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }


    // 게시물 삭제
    @Test
    @WithMockUser
    @DisplayName("게시물을 삭제한다")
    void deletePost() throws Exception {
        //given
        Long postId = 1L;
        String email = "emai@naver.com";

        when(postService.deletePost(1L, email)).thenReturn(postId);

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("게시물이 없으면 게시물 삭제에 실패한다")
    void deletePostFailNoPost() throws Exception {
        //given
        Long postId = 1L;

        doThrow(PostNotFoundException.class)
            .when(postService).deletePost(anyLong(), anyString());

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("작성자가 없으면 게시물 삭제에 실패한다")
    void deletePostFailNoWriter() throws Exception {
        //given
        Long postId = 1L;

        doThrow(MemberNotFoundException.class)
            .when(postService).deletePost(anyLong(), anyString());

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("작성자가 아니면 게시물 삭제에 실패한다")
    void deletePostFailDifferentWriter() throws Exception {
        //given
        Long postId = 1L;

        doThrow(UnauthorizedException.class)
            .when(postService).deletePost(anyLong(), anyString());

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("게시물을 수정한다")
    void modifyPost() throws Exception {
        //given
        Long postId = 1L;
        PostModifyRequestDto dto =
            new PostModifyRequestDto("제목", "내용");
        when(postService.modifyPost(any(PostModifyServiceRequestDto.class))).thenReturn(postId);

        //when then
        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser
    @DisplayName("게시물을 수정할 때, 제목이 없으면 400 에러를 반환한다")
    void modifyPostFailNoTitle(String title) throws Exception {
        //given
        Long postId = 1L;
        PostModifyRequestDto dto =
            new PostModifyRequestDto(title, "내용");

        //when then
        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser
    @DisplayName("게시물을 수정할 때, 내용 없으면 400 에러를 반환한다")
    void modifyPostFailNoContents(String contents) throws Exception {
        //given
        Long postId = 1L;
        PostModifyRequestDto dto =
            new PostModifyRequestDto("제목", contents);

        //when then
        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    private PostDetailDto makePostDetailDto() {
        return new PostDetailDto() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getTitle() {
                return "제목";
            }

            @Override
            public String getContents() {
                return "내용ㅇ";
            }

            @Override
            public String getWriterEmail() {
                return "k@naver.com";
            }

            @Override
            public LocalDateTime getUpdatedAt() {
                return LocalDateTime.of(2023,1,2,3,4);
            }
        };
    }
}