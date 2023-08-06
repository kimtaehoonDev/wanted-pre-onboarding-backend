package com.kimtaehoondev.board.member.presentation.dto;

import com.kimtaehoondev.board.member.application.dto.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberResponse {
    private Long id;
    private String email;

    public static MemberResponse from(MemberInfo memberInfo) {
        return new MemberResponse(memberInfo.getId(), memberInfo.getEmail());
    }
}
