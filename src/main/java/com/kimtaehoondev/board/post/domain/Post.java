package com.kimtaehoondev.board.post.domain;

import com.kimtaehoondev.board.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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


    @ManyToOne
    @JoinColumn(name = "WRITER_ID")
    private Member writer;

    //역정규화가 일어남 writer 객체의 email
    private String writerEmail;


    public static Post create(String title, String contents, Member writer) {
        return new Post(null, title, contents, writer, writer.getEmail());
    }

}