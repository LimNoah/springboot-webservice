package com.lnoah.portfolio.springboot.controller.dto;

import com.lnoah.portfolio.springboot.model.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    //Modelmapper 써볼것
    public PostsResponseDto(Posts entity){
        this.id = entity.getId();;
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
