package com.lnoah.portfolio.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lnoah.portfolio.springboot.controller.dto.PostsSaveRequestDto;
import com.lnoah.portfolio.springboot.controller.dto.PostsUpdateRequestDto;
import com.lnoah.portfolio.springboot.model.posts.Posts;
import com.lnoah.portfolio.springboot.repository.posts.PostsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @After
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @DisplayName("게시글 작성")
    @WithMockUser(roles = "USER")
    @Test
    public void savePosts() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);

//
//        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> allPosts = postsRepository.findAll();
//
//        assertThat(allPosts.get(0).getTitle()).isEqualTo(title);
//        assertThat(allPosts.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("게시글 수정")
    @WithMockUser(roles = "USER")
    @Test
    public void update() throws Exception{
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                .title("제모오오옥")
                .content("내요오오오올")
                .author("자악서엉자아")
                .build());
        Long updateId = savePosts.getId();
        String expectedTitle = "제모오오옥2";
        String expectedContent = "내요오오오옹2";
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
//        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> allPosts = postsRepository.findAll();

        assertThat(allPosts.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(allPosts.get(0).getContent()).isEqualTo(expectedContent);
//        //when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> allPosts = postsRepository.findAll();
//
//        assertThat(allPosts.get(0).getTitle()).isEqualTo(expectedTitle);
//        assertThat(allPosts.get(0).getContent()).isEqualTo(expectedContent);
    }
}