package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다.
//즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 합니다.
@ExtendWith(SpringExtension.class)
//Wen(Spring MVC)에 집중할 수 있는 어노테이션입니다.
//선언할 경우 @Controller, @ControllerAdivce 등을 사용할 수 있다. 단, @Service, @Component, @Repository 등은 사용불가
//여기서는 컨트롤러만 사용하기때문에 선언
@WebMvcTest(controllers = HelloController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
public class HelloControllerTest {

    //스프링이 관리하는 빈(Bean)을 주입 받는다.
    @Autowired
    //웹 API를 테스트할 때 사용, 스프링 MVC 테스트의 시작점
    private MockMvc mvc;

    @WithMockUser(roles="USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        //MockMvc를 통해 /hello 주소로 HTTP GET 요청을한다.(체이닝 기능이 있어 여러가지를 이어서 검증 가능)
        mvc.perform(get("/hello"))
                .andExpect(status().isOk()) //mvc.perform의 결과를 검증, HTTP header의 Status를 검증(여기선 200인지 검증)
                .andExpect(content().string(hello)); //mvc.perform의 결과를 검증, 응답 본문의 내용을 검증, 리턴이 Hello가 맞는지 검증

    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                    get("/hello/dto")
                            .param("name",name) //API테스트할 때 사용될 요청 파라미터를 설정(단 String만 허용)
                            .param("amount"
                                    , String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) //jsonPath > JSON 응답값을 필드별로 검증 가능한 메소드
                .andExpect(jsonPath("$.amount", is(amount))); // $.필드명 으로 명시
    }
}
