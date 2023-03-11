package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.mapper.UserMapper;
import com.squarecross.photoalbum.repository.UserRepository;
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
