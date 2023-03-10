package com.squarecross.photoalbum.controller.view;

import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.service.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

    // 여기서는 검증을 하지 않기 때문에 빨간색이 뜨지 않는 것
    @GetMapping("/add")
    public String addForm(@ModelAttribute UserDto userDto) {
        return "users/loginForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "members/loginForm";
        }
        System.out.println("id = " + userDto.getLoginId());
        System.out.println("password = " + userDto.getPassword());
        System.out.println("OKOK");
        userService.save(userDto);
        return "redirect:/";
    }

}
