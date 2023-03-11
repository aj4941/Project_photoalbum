package com.squarecross.diary.mapper;

import com.squarecross.diary.domain.User;
import com.squarecross.diary.dto.UserDto;

public class UserMapper {

    public static User convertToModel(UserDto userDto){
        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
