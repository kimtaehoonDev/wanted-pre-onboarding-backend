package com.kimtaehoondev.board.post.application;

import com.kimtaehoondev.board.exception.impl.MemberNotFoundException;
import com.kimtaehoondev.board.exception.impl.PostNotFoundException;
import com.kimtaehoondev.board.exception.impl.UnauthorizedException;
import com.kimtaehoondev.board.member.domain.Member;
import com.kimtaehoondev.board.member.domain.repository.MemberRepository;
import com.kimtaehoondev.board.post.application.dto.request.PostModifyServiceRequestDto;
import com.kimtaehoondev.board.post.application.dto.response.PostDetailDto;
import com.kimtaehoondev.board.post.application.dto.response.PostSummaryDto;
import com.kimtaehoondev.board.post.domain.Post;
import com.kimtaehoondev.board.post.domain.PostRepository;
import com.kimtaehoondev.board.post.application.dto.request.PostWriteServiceRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        Member writer = memberRepository.findByEmail(dto.getEmail())
            .orElseThrow(MemberNotFoundException::new);

        Post post = Post.create(dto.getTitle(), dto.getContents(), writer);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Override
    public List<PostSummaryDto> getPostsByPage(Pageable pageable) {
        return postRepository.findAllPostBy(pageable);
    }

    @Override
    public PostDetailDto getPost(Long postId) {
        return postRepository.findPostById(postId)
            .orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public Long deletePost(Long postId, String email) {
        Post post = findAuthorizedMembersPost(postId, email);
        postRepository.delete(post);
        return post.getId();
    }

    @Override
    @Transactional
    public Long modifyPost(PostModifyServiceRequestDto serviceDto) {
        Post post = findAuthorizedMembersPost(serviceDto.getPostId(), serviceDto.getEmail());
        post.changeTitleAndContents(serviceDto.getTitle(), serviceDto.getContents());
        return post.getId();
    }

    private Post findAuthorizedMembersPost(Long postId, String email) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        if (!post.writtenBy(email)) {
            throw new UnauthorizedException();
        }
        return post;
    }
}
