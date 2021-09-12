package com.psychored.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DiscordBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);
    }

}
