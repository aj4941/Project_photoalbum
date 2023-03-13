package com.squarecross.diary.controller.view;

import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
            result.rejectValue("loginId", "Duplicated"); // 이름 중복
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
    public String login(@Valid @ModelAttribute UserDto userDto,
                        BindingResult result,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            return "users/loginForm";
        }

        if (!userService.findLoginId(userDto)) {
            result.rejectValue("loginId", "NotFind"); // 아이디가 존재하지 않는 오류
            return "users/loginForm";
        }

        if (!userService.findLogin(userDto)) {
            result.rejectValue("password", "NotFind"); // 비밀번호가 틀리는 오류
            return "users/loginForm";
        }

        // request를 통해 세션 정보가 있으면 getSession으로 HttpSession이 나왔을 것임
        // 없더라면 새로운 세션을 생성하여 등록
        HttpSession session = request.getSession();
        session.setAttribute("userDto", userDto);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
