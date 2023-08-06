package com.kimtaehoondev.board.member.application;

import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberInfo getMember(Long memberId) {
        return memberRepository.findById(memberId, MemberInfo.class)
            .orElseThrow(MemberNotFoundException::new);
    }
}
