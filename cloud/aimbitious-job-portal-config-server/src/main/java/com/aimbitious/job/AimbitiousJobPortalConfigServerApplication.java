package com.aimbitious.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer //enabling config server to run using this anno.
public class AimbitiousJobPortalConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalConfigServerApplication.class, args);
	}

}
