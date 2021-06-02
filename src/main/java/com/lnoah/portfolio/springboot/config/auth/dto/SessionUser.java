package com.lnoah.portfolio.springboot.config.auth.dto;

import com.lnoah.portfolio.springboot.model.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // SessionUser에는 인증된 사용자 정보만 필요로 하기 때문에 name, email, picture만 필드로 선언
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
