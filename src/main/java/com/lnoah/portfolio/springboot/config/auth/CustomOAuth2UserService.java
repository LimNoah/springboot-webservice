package com.lnoah.portfolio.springboot.config.auth;

import com.lnoah.portfolio.springboot.config.auth.dto.OAuthAttributes;
import com.lnoah.portfolio.springboot.config.auth.dto.SessionUser;
import com.lnoah.portfolio.springboot.model.user.User;
import com.lnoah.portfolio.springboot.repository.posts.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // 로그인 후 불러온 여러 정보들을 가지고 유저 정보를 리턴하는 메소드.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();            // 현재 로그인 진행 중인 서비스를 구분하는 코드.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()     // OAuth2 로그인 진행 시 키가 되는 필드 값. PK같은 값.
                .getUserInfoEndpoint().getUserNameAttributeName();                                  // 네이버, 카카오, 구글 로그인을 동시에 지원할 때 사용.

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // SessionUser 클래스 => 세션에 사용자 정보를 저장하기 위한 DTO 클래스
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // OAuthAttribute 클래스를 받아 email로 User가 있는지 찾고,
    // 찾았다면 User객체에 OAuthAttribute에 있는 name과 picture의 정보를 업데이트하여 User 객체에 담고,
    // 찾지 못했다면 OAuthAttribute의 정보들을 가지고 builder로 User객체를 만들어 준 뒤,
    // 해당 User 객체를 DB에 저장해주는 메소드.
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user =  userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}