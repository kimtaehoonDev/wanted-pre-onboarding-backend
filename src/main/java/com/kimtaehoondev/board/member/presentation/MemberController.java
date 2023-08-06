package com.kimtaehoondev.board.member.presentation;

import com.kimtaehoondev.board.member.application.MemberService;
import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import com.kimtaehoondev.board.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(name = "id") Long memberId) {
        MemberInfo memberInfo = memberService.getMember(memberId);
        return ResponseEntity.ok(MemberResponse.from(memberInfo));
    }
}
