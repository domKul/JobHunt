package com.jobhunt.inftrastructure.security.jwt;

import com.jobhunt.inftrastructure.userloginandregister.controller.JwtResponseDto;
import com.jobhunt.inftrastructure.userloginandregister.controller.TokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator {

    private final AuthenticationManager authenticationManager;

    public JwtResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequestDto.username(),tokenRequestDto.password()));
        return null;
    }
}
