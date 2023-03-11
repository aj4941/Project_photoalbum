package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.mapper.UserMapper;
import com.squarecross.photoalbum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<User> findLogin(UserDto userDto) {
        return userRepository.findByLoginId(userDto.getLoginId());
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }
}
