package com.jobhunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobHuntSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobHuntSpringApplication.class, args);
    }
}
