package com.squarecross.diary.service;

import com.squarecross.diary.domain.User;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.UserMapper;
import com.squarecross.diary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserDto userDto) {
        User user = UserMapper.convertToModel(userDto);
        userRepository.save(user);
    }

    public boolean findLoginId(UserDto userDto) {
        Optional<User> res = userRepository.findByLoginId(userDto.getLoginId());
        return res.isPresent() ? true : false;
    }

    public boolean findLogin(UserDto userDto) {
        Optional<User> res = userRepository.findByLoginId(userDto.getLoginId());
        if (res.isPresent()) {
            User user = res.get();
            return Objects.equals(user.getPassword(), userDto.getPassword());
        }
        return false;
    }
}
