package com.jobhunt.inftrastructure.user.controller;

import com.jobhunt.domain.user.RegisterAndLoginFacade;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.domain.user.dto.UserRegisterResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/token")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final RegisterAndLoginFacade registerAndLoginFacade;

    @PostMapping
    ResponseEntity<UserRegisterResultDto> registerUser(@RequestBody @Valid UserRegisterDto userRegisterDto){
        UserRegisterResultDto user = registerAndLoginFacade.registerUser(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
