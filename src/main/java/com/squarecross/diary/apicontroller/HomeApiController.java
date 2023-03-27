package com.squarecross.diary.apicontroller;

import com.squarecross.diary.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api")
@Slf4j
public class HomeApiController {

    @GetMapping
    public ResponseEntity<UserDto> homeLogin(
            @SessionAttribute(name = "userDto", required = false) UserDto userDto) {

        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
