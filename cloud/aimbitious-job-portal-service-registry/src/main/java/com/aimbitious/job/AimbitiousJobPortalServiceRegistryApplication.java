package com.aimbitious.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //enabling eureka server to run using this anno.
public class AimbitiousJobPortalServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalServiceRegistryApplication.class, args);
	}

}
