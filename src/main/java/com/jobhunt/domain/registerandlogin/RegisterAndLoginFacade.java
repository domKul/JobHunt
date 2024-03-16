package com.jobhunt.domain.registerandlogin;

import com.jobhunt.domain.registerandlogin.dto.UserDto;
import com.jobhunt.domain.registerandlogin.dto.UserRegisterDto;
import com.jobhunt.domain.registerandlogin.dto.UserRegisterResultDto;
import com.jobhunt.domain.registerandlogin.exception.UserExceptionMessage;
import com.jobhunt.domain.registerandlogin.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterAndLoginFacade {

    private final RegisterRepository registerRepository;

    public UserDto findByUserName(String username){
        return registerRepository.findByUsername(username)
                .map(UserMapper::mapUserToDto)
                .orElseThrow(()->new UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND));
    }

    public UserRegisterResultDto register(UserRegisterDto userRegisterDto){
        User savedUser= registerRepository.save(UserMapper.mapToUserFromRegisterDto(userRegisterDto));
        return new UserRegisterResultDto(savedUser.id(),true, savedUser.username());
    }
}
