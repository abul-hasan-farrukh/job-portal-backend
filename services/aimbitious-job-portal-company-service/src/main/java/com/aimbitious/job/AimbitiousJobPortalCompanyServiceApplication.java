package com.aimbitious.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AimbitiousJobPortalCompanyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalCompanyServiceApplication.class, args);
	}
}
