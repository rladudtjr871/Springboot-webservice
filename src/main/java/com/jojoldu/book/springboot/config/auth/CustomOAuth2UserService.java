package com.jojoldu.book.springboot.config.auth;


import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpSession;
import java.util.Collections;

//구글 로그인 이후 가져온 사용자 정보를 기반으로 가입 및 정보수정 세션 저장등의 기능을하는 클래스
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User>
                delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //현재 로그인 진행중인 서비스를 구분하는 코드(구글인지, 네이버인지)
        String userNameAttributeName = userRequest
                                    .getClientRegistration().getProviderDetails()
                                        .getUserInfoEndpoint()
                                            .getUserNameAttributeName(); //OAuth2 로그인 진행시 키가되는 필드값을 이야기한다.(Primary Key)
                                                                        //구글은 지원(sub), 네이버/카카오는 지원x

        OAuthAttributes attributes = OAuthAttributes
                                        .of(registrationId, userNameAttributeName
                                                , oAuth2User.getAttributes()); //OAuth2UserService를 통해 가져온 OAuth2User의
                                                                                //attribute를 담을 클래스입니다.


        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user)); //세션에 사용자 정보를 저장하기 위한 Dto 클래스입니다.

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                        attributes.getAttributes(),
                                        attributes.getNameAttributeKey());

    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
