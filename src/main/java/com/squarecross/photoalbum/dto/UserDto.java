package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
