package com.jobhunt.domain.userloginandregister;

import com.jobhunt.domain.userloginandregister.dto.UserDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterResultDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterAndLoginFacade {

    private final UserService userService;

    public UserDto findUserByUserName(String username){
        return userService.findByUserName(username);
    }

    public UserRegisterResultDto registerUser(UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }
}
