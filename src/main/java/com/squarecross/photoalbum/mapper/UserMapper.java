package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.User;
import com.squarecross.photoalbum.dto.UserDto;

public class UserMapper {

    public static User convertToModel(UserDto userDto){
        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
