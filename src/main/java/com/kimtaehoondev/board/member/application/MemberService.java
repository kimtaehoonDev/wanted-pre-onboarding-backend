package com.kimtaehoondev.board.member.application;

import com.kimtaehoondev.board.member.application.dto.MemberInfo;

public interface MemberService {
    MemberInfo getMember(Long memberId);
}
