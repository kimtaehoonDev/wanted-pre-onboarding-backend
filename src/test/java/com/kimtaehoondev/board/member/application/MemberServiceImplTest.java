package com.kimtaehoondev.board.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버를 조회한다")
    void findMember() {
        Long memberId = 1L;
        String email = "ds@naver.com";
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 3, 4);

        MemberInfo memberInfo = new MemberInfo() {
            @Override
            public Long getId() {
                return memberId;
            }

            @Override
            public String getEmail() {
                return email;
            }

            @Override
            public LocalDateTime getUpdatedAt() {
                return updatedAt;
            }
        };

        when(memberRepository.findById(memberId, MemberInfo.class))
            .thenReturn(Optional.of(memberInfo));

        MemberInfo member = memberService.getMember(memberId);
        assertThat(member.getId()).isEqualTo(memberId);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("존재하지 않는 멤버를 조회하면 MemberNotFoundException을 반환한다")
    void findMemberFailNotFound() {
        Long memberId = 1L;

        doThrow(MemberNotFoundException.class)
            .when(memberRepository).findById(memberId, MemberInfo.class);

        Assertions.assertThatThrownBy(() -> memberService.getMember(memberId))
            .isInstanceOf(MemberNotFoundException.class);
    }

}