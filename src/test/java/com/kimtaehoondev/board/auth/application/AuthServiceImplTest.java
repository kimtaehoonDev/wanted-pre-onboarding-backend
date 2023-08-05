package com.kimtaehoondev.board.auth.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kimtaehoondev.board.auth.jwt.JwtTokenProvider;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void signUp() throws NoSuchFieldException, IllegalAccessException {
        //given
        long savedId = 100L;
        String email = "k@naver.com";
        String pwd = "123456789";

        Member member = makeMember(savedId, email, pwd);

        SignUpRequestDto dto = new SignUpRequestDto(email, "12345678");
        when(memberRepository.findByEmail(email))
            .thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedResult");
        when(memberRepository.save(any(Member.class)))
                .thenReturn(member);

        //when
        Long foundedId = authService.signUp(dto);

        //then
        Assertions.assertThat(foundedId).isEqualTo(savedId);
        verify(memberRepository, times(1)).findByEmail(email);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void emailDuplicated() {
        // given
        String email = "k@naver.com";
        SignUpRequestDto dto = new SignUpRequestDto(email, "12345678");
        when(memberRepository.findByEmail(email))
            .thenReturn(Optional.of(Member.createNormalMember(null, null)));

        // then
        Assertions.assertThatThrownBy(() -> authService.signUp(dto))
            .isInstanceOf(EmailDuplicatedException.class);
        verify(memberRepository, times(1)).findByEmail(email);
        verify(memberRepository, never()).save(any(Member.class));
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