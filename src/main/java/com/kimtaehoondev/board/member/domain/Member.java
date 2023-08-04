package com.kimtaehoondev.board.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private Long id;

    private String email;

    private String pwd;

    public static Member create(String email, String pwd) {
        return new Member(null, email, pwd);
    }
}
