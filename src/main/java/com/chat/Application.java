package com.chat;

import com.chat.cors.EnableCors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCors
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
