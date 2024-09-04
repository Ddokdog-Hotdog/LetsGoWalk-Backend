package com.ddokdoghotdog.gowalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy
public class GowalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GowalkApplication.class, args);
	}

	@Configuration
	@EnableJpaAuditing // JPA Auditing 활성화
	public class JpaConfig {
	}
}
