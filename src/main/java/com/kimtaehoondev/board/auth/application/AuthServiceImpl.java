package com.kimtaehoondev.board.auth.application;

import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;


    @Override
    public Long signUp(SignUpRequestDto dto) {
        memberRepository.findByEmail(dto.getEmail())
            .ifPresent(x -> {
                throw new EmailDuplicatedException();
            });

        Member member = dto.toEntity();
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
