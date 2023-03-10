package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;
import com.squarecross.photoalbum.mapper.UserMapper;
import com.squarecross.photoalbum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserDto memberDto) {
        User member = UserMapper.convertToModel(memberDto);
        System.out.println("member_id = " + member.getLoginId());
        System.out.println("member_password = " + member.getPassword());
        userRepository.save(member);
        log.info("저장되었습니다.");
    }
}
