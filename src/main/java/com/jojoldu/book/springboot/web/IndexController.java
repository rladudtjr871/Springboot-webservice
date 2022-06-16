package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.LoginUser;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.service.posts.PostsService;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    //private final HttpSession httpSession;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) { //서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
        // 여기서는 postsService.findAllDesc()로 가져온 결과를  posts로 index.mustache에 전달한다.
        // 199p >개선: @LoginUser SessionUser > 기존에 httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선
        //이제 어느 컨트롤러든 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 되었다.


        model.addAttribute("posts", postsService.findAllDesc());
        
        //앞서 작성된 CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장하도록 구성
        //즉, 로그인 성공시 httpSession.getAttribute("user")에서 값을 가져올 수 있다.
        //SessionUser user = (SessionUser) httpSession.getAttribute("user");
        
        //세션에 저장된 값이 있으면 model에 userName으로 등록
        if (user != null) {
            model.addAttribute("loginUserName", user.getName());
        }
        
        return "index";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
    
    
    
}
