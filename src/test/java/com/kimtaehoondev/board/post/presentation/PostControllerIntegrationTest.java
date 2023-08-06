package com.kimtaehoondev.board.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.kimtaehoondev.board.post.presentation.dto.PostModifyRequestDto;
import com.kimtaehoondev.board.post.presentation.dto.PostWriteRequestDto;
import com.kimtaehoondev.board.post.presentation.dto.response.PostResponseDto;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    static String loginMemberEmail = "k@naver.com";
    static String loginMemberPwd = "12345678";

    static String otherEmail = "other@naver.com";
    static String otherPwd = "241512561244";


    /**
     * email과 otherEmail 두 계정에 대해 회원가입을 한다
     */
    @BeforeEach
    @WithMockUser(username = "other@naver.com", roles = "USER")
    void beforeEach() throws Exception {
        registerMember(loginMemberEmail, loginMemberPwd);
        registerMember(otherEmail, otherPwd);
    }

    @ParameterizedTest
    @CsvSource({"title, contents", "@, sakljfhajkshfajkfhjakhfjk", " 1, @",
        "sakljfhajkshfajkfhjakhfjk, @"})
    @DisplayName("게시물을 성공적으로 등록한다")
    @WithMockUser(username = "k@naver.com", roles = "USER")
    void writePostHavingTitle(String title, String contents) throws Exception {
        registerPost(title, contents);
    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 게시물을 등록할 때, 401 상태를 반환한다")
    void cantWriteUnauthenticatedMember() throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto("title", "contents");

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 등록할 때 제목이 비어있으면, 400 상태를 반환한다")
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
    @DisplayName("게시물을 등록할 때 내용이 비어있으면, 400 상태를 반환한다")
    void noContents(String contents) throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto("title", contents);

        mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시물을 page 번호를 사용해 조회한다")
    void paging() throws Exception {
        UserDetails otherUser = makeUser(otherEmail, otherPwd);

        // 게시물을 12개 등록합니다
        for (int i = 0; i < 12; i++) {
            registerPost("title", "contents", otherUser);
        }

        //when & then
        // 0페이지를 조회하면 10개의 게시물을 반환합니다(최대 10개)
        int page = 0;
        mockMvc.perform(get("/api/posts?page=" + page).with(csrf())
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(10));

        // 1페이지를 조회하면 2개의 게시물을 반환합니다.
        page = 1;
        mockMvc.perform(get("/api/posts?page=" + page).with(csrf())
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));

        // 2페이지를 조회하면 0개의 게시물을 반환합니다.
        page = 2;
        mockMvc.perform(get("/api/posts?page=" + page).with(csrf())
                .with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }


    // 게시물 단건 읽기
    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물 단건을 조회한다")
    void searchAlone() throws Exception {
        Long postId = registerPost("title", "contents");

        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("존재하지 않는 게시물을 조회하면, 404 상태를 반환한다")
    void searchAloneFail() throws Exception {
        long postId = 123L;

        mockMvc.perform(get("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 삭제한다")
    void deletePost() throws Exception {
        //given
        Long postId = registerPost("title", "content");

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("존재하지 않는 게시물을 삭제하면, 404 상태를 반환한다")
    void deletePostFailNoPost() throws Exception {
        //given
        Long postId = 123L;

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 게시물을 삭제할 때, 401 상태를 반환한다")
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
        Long postId = registerPost("title", "cont", otherUser);

        //when then
        mockMvc.perform(delete("/api/posts/" + postId).with(csrf()))
            .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정한다")
    void modifyPost() throws Exception {
        //given
        Long postId = registerPost("ittle", "con");

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto("변경할거야", "이렇게");

        // 수정이 성공하면 200번 상태를 반환합니다.
        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isOk());

        PostResponseDto response = findPostById(postId);

        assertThat(response.getId()).isEqualTo(postId);
        assertThat(response.getTitle()).isEqualTo(modifyDto.getTitle());
        assertThat(response.getContents()).isEqualTo(modifyDto.getContents());
        assertThat(response.getWriterEmail()).isEqualTo(loginMemberEmail);
    }


    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정할 때 제목이 없으면, 400 상태를 반환한다")
    void modifyPostFailNoTitle(String title) throws Exception {
        //given
        Long postId = registerPost("title", "contents");

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto(title, "이렇게");

        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 게시물을 수정하려 하면, 401 상태를 반환한다")
    void modifyPostFailNoLogin() throws Exception {
        //given
        UserDetails otherUser = makeUser(otherEmail, otherPwd);
        Long postId = registerPost("title", "contents", otherUser);

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto("title", "이렇게");

        //when then
        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "   "})
    @WithMockUser(username = "k@naver.com", roles = "USER")
    @DisplayName("게시물을 수정할 때 내용이 없으면, 400 에러를 반환한다")
    void modifyPostFailNoContents(String contents) throws Exception {
        //given
        Long postId = registerPost("title", "contents");

        PostModifyRequestDto modifyDto =
            new PostModifyRequestDto("바꿀거야", contents);

        mockMvc.perform(put("/api/posts/" + postId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyDto)))
            .andExpect(status().isBadRequest());
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

    private Long registerMember(String email, String pwd) throws Exception {
        SignUpRequestDto userDto = new SignUpRequestDto(email, pwd);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/members/")))
            .andReturn();
        return getCreatedResourceId(mvcResult);
    }

    private PostResponseDto findPostById(Long postId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/posts/" + postId))
            .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PostResponseDto response =
            objectMapper.readValue(contentAsString, PostResponseDto.class);
        return response;
    }

    private Long registerPost(String title, String contents) throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto(title, contents);

        MvcResult mvcResult = mockMvc.perform(post("/api/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/posts/")))
            .andReturn();
        return getCreatedResourceId(mvcResult);
    }

    private Long registerPost(String title, String contents, UserDetails u)
        throws Exception {
        PostWriteRequestDto dto = new PostWriteRequestDto(title, contents);

        MvcResult mvcResult = mockMvc.perform(post("/api/posts").with(csrf())
                .with(user(u))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/posts/")))
            .andReturn();
        return getCreatedResourceId(mvcResult);
    }

    private static Long getCreatedResourceId(MvcResult mvcResult) {
        String location = mvcResult.getResponse().getHeader("Location").substring(9);
        String[] split = location.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

}