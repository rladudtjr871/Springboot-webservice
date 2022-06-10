package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  //Spring Security 설정들을 활성화 시켜줌
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당 옵션들을 disable한다.
                .and()
                    .authorizeRequests()    //URL별 권한 관리를 설정하는 옵션의 시작점 > 이게 선언되어야 antMatchers 옵션 사용가능
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**")
                    .permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //권한 관리 대상을 지정하는 옵션 > "/"등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람권한을 줌ㅁ
                                                                         //"/api/v1/**"주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 했다.
                    .anyRequest().authenticated() //설정된 값들 이외 나머지 URL들을 나타냅니다.
                .and()                            //authenticated()를 통해 로그인된 사용자들에게만 나머지 URL들을 허용함
                    .logout()
                        .logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점 > 로그아웃 성공시 "/" 주소로 이동
                .and()
                    .oauth2Login()      //OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint()     //OAuth2 로그인 성공 후 사용자 정보를 가져올 때의 설정들을 담당
                        .userService(customOAuth2UserService);
                            //소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
                            //리소스 서버(소셜서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자하는 기능을 명시할 수 있다.
    }





}
