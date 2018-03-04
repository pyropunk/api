package net.za.grasser.test.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring boot application main entry point.<br>
 * Requirement 0: Create a rest service with these end-points
 */
@SpringBootApplication
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}