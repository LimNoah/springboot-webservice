package com.lnoah.portfolio.springboot.service.posts;

import com.lnoah.portfolio.springboot.controller.dto.PostsListResponseDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsResponseDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsSaveRequestDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsUpdateRequestDto;
import com.lnoah.portfolio.springboot.exception.ResourceNotFoundException;
import com.lnoah.portfolio.springboot.model.posts.Posts;
import com.lnoah.portfolio.springboot.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts =  postsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts posts =  postsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시글이 없습니다. id = " + id));
        return new PostsResponseDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 게시글이 없습니다. id = " + id));
        postsRepository.delete(posts);
    }
}
