package com.lnoah.portfolio.springboot.config.auth;

import com.lnoah.portfolio.springboot.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 소셜 로그인 이후 가져온 사용자 정보들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원하는 Service 클래스.
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable
                .and()
                .authorizeRequests()                // URL별 권한 관리를 설정하는 옵션의 시작점. antMatchers로 권한 설정. HTTP, URL 메소드 별로 관리 가능
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()                           // 로그아웃 기능에 대한 설정 진입점. logoutSuccesUrl로 로그아웃 성공 이후의 주소를 지정
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()                      // OAuth2 로그인 기능에 대한 설정 진입점.
                .userInfoEndpoint()                 // userInfoEndpoint로 로그인 성공 이후 사용자 정보를 가져올 때 설정들을 담당
                .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속조치를 진행할 UserService 인터페이스의 구현체를 등록.
                                                       // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시 가능.
    }
}
