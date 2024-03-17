package com.jobhunt.domain.user;

import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;

class UserMapper {

    static UserDto mapUserToDto(User user){
        return new UserDto(user.id(), user.username(),user.password());
    }

    static User mapToUserFromRegisterDto(UserRegisterDto userRegisterDto){
        return User.builder()
                .username(userRegisterDto.username())
                .password(userRegisterDto.password())
                .build();
    }
}
