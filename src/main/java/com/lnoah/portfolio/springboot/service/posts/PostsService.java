package com.lnoah.portfolio.springboot.service.posts;

import com.lnoah.portfolio.springboot.controller.dto.PostsSaveRequestDto;
import com.lnoah.portfolio.springboot.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }
}
