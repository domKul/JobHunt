package com.jobhunt.domain.user;

import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.domain.user.dto.UserRegisterResultDto;
import com.jobhunt.domain.user.exception.UserExceptionMessage;
import com.jobhunt.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterAndLoginFacade {

    private final RegisterRepository registerRepository;

    public UserDto findByUserName(String username){
        return registerRepository.findByUsername(username)
                .map(UserMapper::mapUserToDto)
                .orElseThrow(()->new UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));
    }

    public UserRegisterResultDto register(UserRegisterDto userRegisterDto){
        User savedUser= registerRepository.save(UserMapper.mapToUserFromRegisterDto(userRegisterDto));
        return new UserRegisterResultDto(savedUser.id(),true, savedUser.username());
    }
}
