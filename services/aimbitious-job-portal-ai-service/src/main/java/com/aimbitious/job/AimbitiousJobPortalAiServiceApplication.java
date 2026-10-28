package com.aimbitious.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class AimbitiousJobPortalAiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalAiServiceApplication.class, args);
	}

}
