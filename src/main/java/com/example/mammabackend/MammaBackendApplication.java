package com.example.mammabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MammaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MammaBackendApplication.class, args);
    }

}
