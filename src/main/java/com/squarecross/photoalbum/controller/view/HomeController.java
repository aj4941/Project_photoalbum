package com.squarecross.photoalbum.controller.view;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.util.logging.impl.LoggerInfoStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Controller
@RequestMapping
@Slf4j
public class HomeController {

    @Autowired UserService userService;

    @GetMapping("/")
    public String homeLogin(
            // 로그인 하지않은 사용자도 들어올 수 있게 + String 쿠키를 타입 컨버팅으로 Long 변환
            @CookieValue(name = "userId", required = false) Long userId, Model model) {

        if (userId == null) {
            log.info("userId is Null");
            return "home";
        }
        Optional<User> res = userService.findUser(userId);
        if (res.isEmpty()) {
            log.info("isEmpty");
            return "home";
        }

        User user = res.get();
        model.addAttribute("user", user);
        return "loginHome";
    }

}
