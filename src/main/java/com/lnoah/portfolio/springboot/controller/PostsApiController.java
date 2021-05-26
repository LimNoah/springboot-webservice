package com.lnoah.portfolio.springboot.controller;

import com.lnoah.portfolio.springboot.controller.dto.PostsResponseDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsSaveRequestDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsUpdateRequestDto;
import com.lnoah.portfolio.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<Long> save(@RequestBody PostsSaveRequestDto requestDto) {
        return ResponseEntity.ok(postsService.save(requestDto));
    }

    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return ResponseEntity.ok(postsService.update(id, requestDto));
    }

    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostsResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(postsService.findById(id));
    }
}