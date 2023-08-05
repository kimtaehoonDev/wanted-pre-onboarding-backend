package com.kimtaehoondev.board.post.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kimtaehoondev.board.exception.MemberNotFoundException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import com.kimtaehoondev.board.post.domain.Post;
import com.kimtaehoondev.board.post.domain.PostRepository;
import com.kimtaehoondev.board.post.application.dto.request.PostWriteServiceRequestDto;
import java.lang.reflect.Field;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("게시물을 작성한다")
    void writePost() throws NoSuchFieldException, IllegalAccessException {
        //given
        String title = "제목";
        String contents = "내용입니다";
        Long postId = 12L;
        Long writerId = 1L;
        Member writer = makeMember(writerId, "k@naver.com", "123456789");
        Post post = makePost(postId, title, contents, writer);
        PostWriteServiceRequestDto dto = new PostWriteServiceRequestDto(title, contents, writer.getEmail());

        when(memberRepository.findByEmail(writer.getEmail()))
            .thenReturn(Optional.of(writer));
        when(postRepository.save(any(Post.class)))
            .thenReturn(post);

        //when
        Long savedPostId = postService.writePost(dto);

        //then
        Assertions.assertThat(savedPostId).isEqualTo(post.getId());
        verify(memberRepository, times(1)).findByEmail(writer.getEmail());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 등록 시 작성자가 존재하지 않으면 예외를 반환한다")
    void noWriter() {
        //given
        String title = "제목";
        String contents = "내용입니다";
        String email = "k@naver.com";
        PostWriteServiceRequestDto dto = new PostWriteServiceRequestDto(title, contents, email);

        when(memberRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.empty());

        //when, then
        Assertions.assertThatThrownBy(() -> postService.writePost(dto))
            .isInstanceOf(MemberNotFoundException.class);
        verify(memberRepository, times(1)).findByEmail(email);
        verify(postRepository, never()).save(any(Post.class));
    }

    private static Post makePost(long id, String title, String content, Member writer)
        throws NoSuchFieldException, IllegalAccessException {
        Post post = Post.create("title", "content", writer);

        Field idField = Post.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(post, id);

        return post;
    }
    private static Member makeMember(long savedId, String email, String pwd)
        throws NoSuchFieldException, IllegalAccessException {
        Member member = Member.createNormalMember(email, pwd);

        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, savedId);

        return member;
    }
}