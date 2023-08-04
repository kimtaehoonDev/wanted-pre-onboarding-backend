package com.kimtaehoondev.board.member.domain.repository;

import com.kimtaehoondev.board.member.domain.Member;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);
}
