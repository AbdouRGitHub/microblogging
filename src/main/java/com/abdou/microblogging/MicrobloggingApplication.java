package com.abdou.microblogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MicrobloggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrobloggingApplication.class, args);
	}

}
