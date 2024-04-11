package com.jobhunt.inftrastructure.userloginandregister.controller;

import com.jobhunt.domain.userloginandregister.RegisterAndLoginFacade;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterAndLoginFacade registerAndLoginFacade;
    private final PasswordEncoder bCryptPasswordEncoder;


    @PostMapping
    public ResponseEntity<UserRegisterResultDto> register(@RequestBody UserRegisterDto registerUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        UserRegisterResultDto registerResult = registerAndLoginFacade.registerUser(
                new UserRegisterDto(registerUserDto.username(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResult);
    }
}
