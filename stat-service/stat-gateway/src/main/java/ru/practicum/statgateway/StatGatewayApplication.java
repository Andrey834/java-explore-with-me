package ru.practicum.statgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class StatGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatGatewayApplication.class, args);
    }

}
