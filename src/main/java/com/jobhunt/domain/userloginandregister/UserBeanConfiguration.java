package com.jobhunt.domain.userloginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserBeanConfiguration {

    @Bean
    UserService userService(RegisterRepository registerRepository){
        return new UserService(registerRepository);
    }

    @Bean
    RegisterAndLoginFacade registerAndLoginFacade(UserService userService){
        return new RegisterAndLoginFacade(userService);
    }
}
