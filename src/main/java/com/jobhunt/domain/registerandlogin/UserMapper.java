package com.jobhunt.domain.registerandlogin;

import com.jobhunt.domain.registerandlogin.dto.UserDto;
import com.jobhunt.domain.registerandlogin.dto.UserRegisterDto;

class UserMapper {

    static UserDto mapUserToDto(User user){
        return new UserDto(user.id(), user.password(), user.username());
    }

    static User mapToUserFromRegisterDto(UserRegisterDto userRegisterDto){
        return User.builder()
                .username(userRegisterDto.username())
                .password(userRegisterDto.password())
                .build();
    }
}
