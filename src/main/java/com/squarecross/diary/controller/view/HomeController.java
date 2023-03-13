package com.squarecross.diary.controller.view;

import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@Slf4j
public class HomeController {

    @Autowired UserService userService;

    @GetMapping("/")
    public String homeLogin(
            @SessionAttribute(name = "userDto", required = false) UserDto userDto, Model model) {

        if (userDto == null) {
            return "home";
        }

        model.addAttribute("userDto", userDto);
        return "loginHome";
    }
}
