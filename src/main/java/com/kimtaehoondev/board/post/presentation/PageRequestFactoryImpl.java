package com.kimtaehoondev.board.post.presentation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * page가 null이나 음수가 들어오면 0으로 변경해준다
 * size는 고정값을 사용한다
 */
@Component
public class PageRequestFactoryImpl implements PageRequestFactory {
    public static final int size = 10; //TODO properties로 빼기

    @Override
    public Pageable make(Integer page) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }

        return PageRequest.of(page, size);
    }
}
