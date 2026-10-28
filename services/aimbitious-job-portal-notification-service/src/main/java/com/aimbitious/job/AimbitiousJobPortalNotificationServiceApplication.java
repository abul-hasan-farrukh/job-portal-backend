package com.aimbitious.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class AimbitiousJobPortalNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalNotificationServiceApplication.class, args);
	}

}
