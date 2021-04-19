package com.lnoah.portfolio.springboot.repository.posts;

import com.lnoah.portfolio.springboot.model.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
