package com.kimtaehoondev.board.member.domain.repository;

import com.kimtaehoondev.board.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    <T> Optional<T> findById(Long memberId, Class<T> type);
}
