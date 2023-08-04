package com.kimtaehoondev.board.post.domain;

import com.kimtaehoondev.board.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private String title;

    private String contents;

    // TODO 매핑
    @ManyToOne
    private Member member;

    public static Post create(String title, String contents, Member writer) {
        return new Post(null, title, contents, writer);
    }

}