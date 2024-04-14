package com.jobhunt;

import com.jobhunt.inftrastructure.security.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableMongoRepositories
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class JobHuntSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobHuntSpringApplication.class, args);
    }
}
