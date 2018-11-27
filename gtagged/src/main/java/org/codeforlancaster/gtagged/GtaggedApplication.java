package org.codeforlancaster.gtagged;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GtaggedApplication {

    public static void main(String[] args) {
        SpringApplication.run(GtaggedApplication.class, args);
    }
}
