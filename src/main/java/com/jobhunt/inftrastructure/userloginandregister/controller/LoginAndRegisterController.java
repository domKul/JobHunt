package com.jobhunt.inftrastructure.userloginandregister.controller;

import com.jobhunt.domain.userloginandregister.RegisterAndLoginFacade;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterResultDto;
import com.jobhunt.inftrastructure.security.jwt.JwtAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final JwtAuthenticator jwtAuthenticator;
    private final RegisterAndLoginFacade registerAndLoginFacade;


    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto>authAndGenerateToken(@RequestBody @Valid TokenRequestDto tokenRequestDto){
        JwtResponseDto generateToken = jwtAuthenticator.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResultDto>registrationUser(@RequestBody UserRegisterDto userRegisterDto){
        UserRegisterResultDto registration = registerAndLoginFacade.registerUser(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }
}
