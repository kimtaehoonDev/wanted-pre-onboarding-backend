package com.kimtaehoondev.board.member.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import com.kimtaehoondev.board.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 멤버를 조회한다")
    void findMemberUsingEmail() {
        String email = "1@naver.com";
        String pwd = "12345678";
        memberRepository.save(Member.createNormalMember(email, pwd));
        memberRepository.save(Member.createNormalMember("2@naver.com", "12345678"));

        Member foundMember = memberRepository.findByEmail(email).get();
        assertThat(foundMember.getEmail()).isEqualTo(email);
        assertThat(foundMember.getPwd()).isEqualTo(pwd);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 멤버를 조회하면 Optional.empty()를 반환한다")
    void notFoundMember() {
        String email = "1@naver.com";
        String pwd = "12345678";
        memberRepository.save(Member.createNormalMember(email, pwd));

        Optional<Member> memberOpt = memberRepository.findByEmail("notregister@naver.com");
        assertThat(memberOpt).isEmpty();
    }

    @Test
    @DisplayName("아이디와 Class 정보를 사용해 멤버를 조회한다")
    void findMemberUsingIdAndClass() {
        String email = "1@naver.com";
        String pwd = "12345678";
        Member savedMember = memberRepository.save(Member.createNormalMember(email, pwd));

        MemberInfo result =
            memberRepository.findById(savedMember.getId(), MemberInfo.class).get();
        assertThat(result.getId()).isEqualTo(savedMember.getId());
        assertThat(result.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("아이디와 Class 정보로 멤버를 조회했지만, 멤버가 존재하지 않으면 Optional.empty()를 반환한다")
    void findMemberUsingIdAndClassNotFound() {
        Optional<MemberInfo> resultOpt = memberRepository.findById(123L, MemberInfo.class);
        assertThat(resultOpt).isEmpty();
    }
}