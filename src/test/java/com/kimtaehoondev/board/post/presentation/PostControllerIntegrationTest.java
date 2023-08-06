package com.kimtaehoondev.board.post.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.post.application.dto.request.PostWriteServiceRequestDto;
import com.kimtaehoondev.board.post.presentation.dto.PostModifyRequestDto;
import com.kimtaehoondev.board.post.presentation.dto.PostWriteRequestDto;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static String email = "k@naver.com";
    static String pwd = "12345678";

    static String otherEmail = "other@naver.com";
    static String otherPwd = "241512561244";

    @BeforeEach
    @WithMockUser(username = "other@naver.com",  roles = "USER")
    void registerMember() throws Exception {
        SignUpRequestDto userDto = new SignUpRequestDto(email, pwd);
        mockMvc.perform(post("/api/auth/signup").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)));

        SignUpRequestDto otherUserDto = new SignUpRequestDto(otherEmail, otherPwd);
        mockMvc.perform(post("/api/auth/signup").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(otherUserDto)));

    }

    // 게시물 생성
    @ParameterizedTest
    @ValueSource(strings = {"title", "@", " 1 ", "sakljfhajkshfajkfhjakhfjk"})
    @DisplayName("게시물 생성 성공 - 제목이 있는 경우")
    @WithMockUser(username = "k@naver.com", roles = "USER")
    void writePostHavingTitle(String title) throws Exception {
        PostWriteRequestDto dto =
            new PostWriteRequestDto(title, "contents");

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/posts/")));

    }

    @ParameterizedTest
    @ValueSource(strings = {"contents", "@", " 1 ", "sakljfhajkshfajkfhjakhfjk"})
    @DisplayName("게시물 생성 성공 - 내용이 있는 경우")
    @WithMockUser(username = "k@naver.com", roles = "USER")
    void writePostHavingContents(String contents) throws Exception {
        PostWriteRequestDto dto =
            new PostWriteRequestDto("title", contents);

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/posts/")));
    }


    @Test
    @DisplayName("로그인되지 않은 사용자가 게시물을 작성할 때 401 상태를 반환한다")
    void cantWriteUnauthenticatedMember() throws Exception {
        String email = "emai@naver.com";
        PostWriteServiceRequestDto dto =
            new PostWriteServiceRequestDto("title", "contents", email);

        mockMvc.perform(post("/api/posts").with(csrf())
                .with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
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
    @WithMockUser(username = "k@naver.com", roles = "USER")
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

        mockMvc.perform(get("/api/posts?page=" + page).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray()); // 최상단이 배열임을 확인한다
    }

    // 게시물 단건 읽기
    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("단건 게시물 조회에 성공한다")
    void searchAlone() throws Exception {
        PostWriteRequestDto dto =
            new PostWriteRequestDto("제목", "contents");

        MvcResult mvcResult = mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andReturn();


        long postId = getCreatedResourceId(mvcResult);

        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("게시물이 없으면 단건 게시물 조회에 실패한다")
    void searchAloneFail() throws Exception {
        long postId = 123L;

        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }


    // 게시물 삭제
    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 삭제한다")
    void deletePost() throws Exception {
        //given
        PostWriteRequestDto dto =
            new PostWriteRequestDto("제목", "contents");
        MvcResult mvcResult = mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andReturn();
        long postId = getCreatedResourceId(mvcResult);

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물이 없으면 게시물 삭제에 실패한다")
    void deletePostFailNoPost() throws Exception {
        //given
        Long postId = 123L;

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("존재하지 않는 사용자는 게시물 삭제에 실패한다")
    void deletePostFailNoWriter() throws Exception {
        //given
        Long postId = 123L;

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf())
                .with(anonymous()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("작성자가 아니면 게시물 삭제에 실패한다")
    void deletePostFailDifferentWriter() throws Exception {
        UserDetails otherUser = makeUser(otherEmail, otherPwd);

        // 다른 사용자 otherUser 객체가 글을 등록한다
        PostWriteRequestDto postDto = new PostWriteRequestDto("title", "contents");
        MvcResult mvcResult = mockMvc.perform(post("/api/posts").with(csrf())
                .with(user(otherUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
            .andReturn();
        long postId = getCreatedResourceId(mvcResult);

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정한다")
    void modifyPost() throws Exception {
        //given
        PostWriteRequestDto dto =
            new PostWriteRequestDto("제목", "내용");

        //when then
        MvcResult mvcResult = mockMvc.perform(post("/api/posts/").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andReturn();
        long postId = getCreatedResourceId(mvcResult);

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto("변경할거야", "이렇게");

        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정할 때, 제목이 없으면 400 에러를 반환한다")
    void modifyPostFailNoTitle(String title) throws Exception {
        //given
        PostWriteRequestDto dto =
            new PostWriteRequestDto("제목", "내용");

        //when then
        MvcResult mvcResult = mockMvc.perform(post("/api/posts/").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andReturn();
        long postId = getCreatedResourceId(mvcResult);

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto(title, "이렇게");

        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정할 때, 내용 없으면 400 에러를 반환한다")
    void modifyPostFailNoContents(String contents) throws Exception {
        //given
        PostWriteRequestDto dto =
            new PostWriteRequestDto("제목", "내용");

        //when then
        MvcResult mvcResult = mockMvc.perform(post("/api/posts/").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andReturn();
        long postId = getCreatedResourceId(mvcResult);

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto("제목", contents);

        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isBadRequest());
    }


    private static long getCreatedResourceId(MvcResult mvcResult) {
        String location = mvcResult.getResponse().getHeader("Location").substring(9);
        String[] split = location.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    private UserDetails makeUser(String email, String pwd) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }

            @Override
            public String getPassword() {
                return pwd;
            }

            @Override
            public String getUsername() {
                return email;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };
    }

}