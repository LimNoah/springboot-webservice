package com.lnoah.portfolio.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// User Session 값을 가져오기 위한 코드의 중복을 방지 하기 위해 메소드 인자로 Session 값을 받아 올 수 있도록 하기 위한 인터페이스
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
