package com.jobhunt.inftrastructure.security;

import com.jobhunt.domain.userloginandregister.RegisterAndLoginFacade;
import com.jobhunt.domain.userloginandregister.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final RegisterAndLoginFacade registerAndLoginFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto dto = registerAndLoginFacade.findUserByUserName(username);
        return getUser(dto) ;
    }

    private User getUser(UserDto userDto){
        return new User(
                userDto.username(),
                userDto.password(),
                Collections.emptyList());
    }
}
