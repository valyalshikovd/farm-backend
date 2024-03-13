package com.example.farmbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FarmBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmBackendApplication.class, args);
	}

}
