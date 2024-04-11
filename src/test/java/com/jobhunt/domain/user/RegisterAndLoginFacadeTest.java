package com.jobhunt.domain.user;

import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.domain.user.dto.UserRegisterResultDto;
import com.jobhunt.domain.user.exception.UserExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class RegisterAndLoginFacadeTest {

    RegisterAndLoginFacade registerAndLoginFacade = new RegisterAndLoginFacade(
            new UserService(new InMemoryLoginAndRegisterRepository())
    );

    private UserRegisterDto userRegisterDto;

    @BeforeEach
    void setUp() {
        userRegisterDto = new UserRegisterDto("userTest","passwordTest");
    }

    @Test
    void shouldSaveUser_Successfully() {
        //Given in setUp

        //When
        UserRegisterResultDto register = registerAndLoginFacade.registerUser(userRegisterDto);
        //Then
        assertAll(
                ()-> assertTrue(register.created()),
                ()->assertEquals(userRegisterDto.username(),register.username())
        );
    }

    @Test
    void shouldFindUserByUsername_Successfully(){
        //Given
        registerAndLoginFacade.registerUser(userRegisterDto);
        //When
        UserDto findUser = registerAndLoginFacade.findUserByUserName("userTest");
        //Then
        assertAll(
                ()->assertNotNull(findUser),
                ()->assertEquals("userTest",findUser.username())
        );
    }

    @Test
    void shouldThrowExceptionIfUserWithUsernameNotExist(){
        //Given
        String wrongUsername = "asdas";
        //When
        BadCredentialsException userNotFound = assertThrows(BadCredentialsException.class,
                () -> registerAndLoginFacade.findUserByUserName(wrongUsername));
        //Then
        assertAll(
                ()->assertNotNull(userNotFound),
                ()->assertEquals(UserExceptionMessage.USER_NOT_FOUND.getMessage(),userNotFound.getMessage())
        );
    }
}