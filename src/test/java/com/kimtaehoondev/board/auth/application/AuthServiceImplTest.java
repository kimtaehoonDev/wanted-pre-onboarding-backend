package com.kimtaehoondev.board.auth.application;

import static org.mockito.Mockito.when;

import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    void signUp() {
        //given
        long savedId = 100L;
        SignUpRequestDto dto = new SignUpRequestDto("k@naver.com", "12345678");

        //when
        Long foundedId = authService.signUp(dto);

        //then
        Assertions.assertThat(foundedId).isEqualTo(savedId);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void emailDuplicated() {
        // given
        long savedId = 100L;
        String email = "k@naver.com";
        SignUpRequestDto dto = new SignUpRequestDto(email, "12345678");
        when(memberRepository.findByEmail(email))
            .thenReturn(Optional.of(Member.create(null, null)));

        // then
        Assertions.assertThatThrownBy(() -> authService.signUp(dto))
            .isInstanceOf(EmailDuplicatedException.class);
    }


}