package com.kimtaehoondev.board.post.presentation;

import org.springframework.data.domain.Pageable;

public interface PageRequestFactory {
    Pageable make(Integer page);

}
