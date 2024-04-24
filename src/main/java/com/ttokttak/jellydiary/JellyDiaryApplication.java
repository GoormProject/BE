package com.ttokttak.jellydiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JellyDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(JellyDiaryApplication.class, args);
    }

}
