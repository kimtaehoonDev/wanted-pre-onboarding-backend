package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.exception.MemberNotFoundException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import com.kimtaehoondev.board.post.domain.Post;
import com.kimtaehoondev.board.post.domain.PostRepository;
import com.kimtaehoondev.board.post.application.dto.PostWriteServiceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long writePost(PostWriteServiceRequestDto dto) {
        Member writer = memberRepository.findById(dto.getMemberId())
            .orElseThrow(MemberNotFoundException::new);

        Post post = Post.create(dto.getTitle(), dto.getContents(), writer);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }
}
