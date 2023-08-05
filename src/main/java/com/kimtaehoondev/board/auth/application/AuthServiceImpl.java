package com.kimtaehoondev.board.auth.application;

import com.kimtaehoondev.board.auth.jwt.JwtTokenProvider;
import com.kimtaehoondev.board.auth.domain.TokenInfo;
import com.kimtaehoondev.board.auth.presentation.dto.SignUpRequestDto;
import com.kimtaehoondev.board.exception.EmailDuplicatedException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public Long signUp(SignUpRequestDto dto) {
        memberRepository.findByEmail(dto.getEmail())
            .ifPresent(x -> {
                throw new EmailDuplicatedException();
            });

        String encodedPwd = passwordEncoder.encode(dto.getPwd());
        Member member = Member.createNormalMember(dto.getEmail(), encodedPwd);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Transactional
    @Override
    public TokenInfo login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication =
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}