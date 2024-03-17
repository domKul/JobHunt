package com.jobhunt.domain.user;

import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.domain.user.dto.UserRegisterResultDto;
import com.jobhunt.domain.user.exception.UserExceptionMessage;
import com.jobhunt.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterAndLoginFacadeTest {

    RegisterAndLoginFacade registerAndLoginFacade = new RegisterAndLoginFacade(
            new InMemoryLoginAndRegisterRepository()
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
        UserRegisterResultDto register = registerAndLoginFacade.register(userRegisterDto);
        //Then
        assertAll(
                ()-> assertTrue(register.created()),
                ()->assertEquals(userRegisterDto.username(),register.username())
        );
    }

    @Test
    void shouldFindUserByUsername_Successfully(){
        //Given
        registerAndLoginFacade.register(userRegisterDto);
        //When
        UserDto findUser = registerAndLoginFacade.findByUserName("userTest");
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
        UserNotFoundException userNotFound = assertThrows(UserNotFoundException.class,
                () -> registerAndLoginFacade.findByUserName(wrongUsername));
        //Then
        assertAll(
                ()->assertNotNull(userNotFound),
                ()->assertEquals(UserExceptionMessage.USER_NOT_FOUND.getMessage(),userNotFound.getMessage())
        );
    }
}