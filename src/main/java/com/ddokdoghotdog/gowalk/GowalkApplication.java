package com.ddokdoghotdog.gowalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class GowalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GowalkApplication.class, args);
	}

	@Configuration
	@EnableJpaAuditing // JPA Auditing 활성화
	public class JpaConfig {
	}
}
