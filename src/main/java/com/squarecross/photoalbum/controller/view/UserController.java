package com.squarecross.photoalbum.controller.view;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // 여기서는 검증을 하지 않기 때문에 빨간색이 뜨지 않는 것
    @GetMapping("/add")
    public String addForm(@ModelAttribute UserDto userDto) {
        return "users/addForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {
            return "users/addForm";
        }
        if (userService.findLoginId(userDto)) {
            // 이름이 같은 회원이 존재합니다 출력 필요
            return "users/addForm";
        }
        userService.save(userDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute UserDto userDto) {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserDto userDto, BindingResult result,
                        HttpServletResponse response) {

        if (result.hasErrors()) {
            return "users/loginForm";
        }

        if (!userService.findLoginId(userDto)) {
            // 존재하지 않는 아이디입니다 출력 필요
            return "users/loginForm";
        }

        Optional<User> res = userService.findLogin(userDto);

        if (res.isEmpty()) {
            // 비밀번호가 틀렸습니다 출력 필요
            return "users/loginForm";
        }

        User user = res.get();
        // cookie name : userId, value : userId_value(string type 필수)
        CookieGenerator cg = new CookieGenerator();
        cg.setCookieName("userId");
        cg.addCookie(response, String.valueOf(user.getUserId()));
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        CookieGenerator cg = new CookieGenerator();
        cg.setCookieName("userId");
        cg.setCookieMaxAge(0);
        cg.addCookie(response, null);
        return "redirect:/";
    }
}
