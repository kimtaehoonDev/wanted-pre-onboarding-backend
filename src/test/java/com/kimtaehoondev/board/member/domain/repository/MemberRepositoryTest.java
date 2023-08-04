package com.kimtaehoondev.board.member.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.kimtaehoondev.board.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 멤버를 조회한다")
    void findMemberUsingEmail() {
        String email = "1@naver.com";
        String pwd = "12345678";
        memberRepository.save(Member.create(email, pwd));
        memberRepository.save(Member.create("2@naver.com", "12345678"));

        Member foundMember = memberRepository.findByEmail(email).get();
        Assertions.assertThat(foundMember.getEmail()).isEqualTo(email);
        Assertions.assertThat(foundMember.getPwd()).isEqualTo(pwd);
    }


}