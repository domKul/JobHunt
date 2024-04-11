package com.jobhunt.domain.userloginandregister;

import com.jobhunt.domain.userloginandregister.dto.UserDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterDto;

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
