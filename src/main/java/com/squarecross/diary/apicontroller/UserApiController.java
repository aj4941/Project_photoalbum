package com.squarecross.diary.apicontroller;

import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public ResponseEntity<String> addForm() {
        return new ResponseEntity<>("addForm", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.findLoginId(userDto)) {
            result.rejectValue("loginId", "Duplicated");
            return new ResponseEntity<>(result.getFieldErrors(),
                    HttpStatus.BAD_REQUEST);
        }

        userService.save(userDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginForm() {
        return ResponseEntity.ok("loginForm");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDto userDto,
                                   BindingResult result,
                                   HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(),
                    HttpStatus.BAD_REQUEST);
        }

        if (!userService.findLoginId(userDto)) {
            result.rejectValue("password", "NotFind");
            return new ResponseEntity<>(result.getFieldErrors(),
                    HttpStatus.BAD_REQUEST);
        }

        if (!userService.findLogin(userDto)) {
            result.rejectValue("password", "NotFind");
            return new ResponseEntity<>(result.getFieldErrors(),
                    HttpStatus.BAD_REQUEST);
        }

        HttpSession session = request.getSession();
        session.setAttribute("userDto", userDto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
