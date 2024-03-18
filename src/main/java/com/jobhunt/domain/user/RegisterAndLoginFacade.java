package com.jobhunt.domain.user;

import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.domain.user.dto.UserRegisterResultDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterAndLoginFacade {

    private final UserService userFetcher;

    public UserDto findUserByUserName(String username){
        return userFetcher.findByUserName(username);
    }

    public UserRegisterResultDto registerUser(UserRegisterDto userRegisterDto){
        return userFetcher.register(userRegisterDto);
    }
}
