package com.kimtaehoondev.board.auth.application;

import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
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
}