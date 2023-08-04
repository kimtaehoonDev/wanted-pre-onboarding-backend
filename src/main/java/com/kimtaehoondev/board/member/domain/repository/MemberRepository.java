package com.kimtaehoondev.board.member.domain.repository;

import com.kimtaehoondev.board.member.domain.Member;
import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);

    Optional<Member> findByEmail(String email);
}
