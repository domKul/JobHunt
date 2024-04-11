package com.jobhunt.inftrastructure.user.controller;

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
@RequestMapping("/token")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final JwtAuthenticator jwtAuthenticator;


    @PostMapping
    public ResponseEntity<JwtResponseDto>authAndGenerateToken(@RequestBody @Valid TokenRequestDto tokenRequestDto){
        JwtResponseDto generateToken = jwtAuthenticator.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken);
    }
}
