package com.lnoah.portfolio.springboot.repository.posts;

import com.lnoah.portfolio.springboot.model.user.SNS;
import com.lnoah.portfolio.springboot.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSns(String email, SNS sns);
}
