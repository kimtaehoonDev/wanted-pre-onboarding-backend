package com.kimtaehoondev.board.member.domain;

import com.kimtaehoondev.board.BaseEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String pwd;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public static Member createNormalMember(String email, String pwd) {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        return new Member(null, email, pwd, roles);
    }
}
