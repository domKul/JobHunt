package com.jobhunt.inftrastructure.userloginandregister.controller;

import com.jobhunt.inftrastructure.security.JwtAuthenticator;
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
class JwtTokenController {

    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping
     ResponseEntity<JwtResponseDto> authAndGenerateToken(@RequestBody @Valid TokenRequestDto tokenRequestDto){
        JwtResponseDto generateToken = jwtAuthenticator.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken);
    }
}
