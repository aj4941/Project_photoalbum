package com.squarecross.photoalbum.controller.view;

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

import javax.validation.Valid;

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
    public String login(@Valid @ModelAttribute UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "users/loginForm";
        }
        if (!userService.findLoginId(userDto)) {
            // 존재하지 않는 아이디입니다 출력 필요
            return "users/loginForm";
        }
        log.info("확인 완료");
        if (!userService.findLogin(userDto)) {
            // 비밀번호가 맞지 않습니다 출력 필요
            return "users/loginForm";
        }


        return "redirect:/";
    }
}
