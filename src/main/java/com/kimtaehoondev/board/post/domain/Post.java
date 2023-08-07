package com.kimtaehoondev.board.post.domain;

import com.kimtaehoondev.board.BaseEntity;
import com.kimtaehoondev.board.member.domain.Member;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE posts SET deleted = true where post_id = ?")
@Where(clause = "deleted = false")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @Getter
    private Long id;

    private String title;

    @Lob
    private String contents;


    @ManyToOne
    @JoinColumn(name = "WRITER_ID")
    private Member writer;

    //역정규화 컬럼
    private String writerEmail;

    private boolean deleted;

    public static Post create(String title, String contents, Member writer) {
        return new Post(null, title, contents, writer, writer.getEmail(), false);
    }

    public boolean writtenBy(String email) {
        return Objects.equals(writerEmail, email);
    }

    public void changeTitleAndContents(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}