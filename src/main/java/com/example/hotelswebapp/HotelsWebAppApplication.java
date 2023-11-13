package com.example.hotelswebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelsWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelsWebAppApplication.class, args);
    }

}
