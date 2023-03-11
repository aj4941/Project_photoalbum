package com.squarecross.photoalbum.controller.view;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.util.logging.impl.LoggerInfoStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping
@Slf4j
public class HomeController {

    @Autowired UserService userService;

    @GetMapping("/")
    public String homeLogin(
            @SessionAttribute(name = "user", required = false) UserDto userDto,
            Model model) {
        if (userDto == null) {
            return "home";
        }

        model.addAttribute("userDto", userDto);
        return "loginHome";
    }
}
