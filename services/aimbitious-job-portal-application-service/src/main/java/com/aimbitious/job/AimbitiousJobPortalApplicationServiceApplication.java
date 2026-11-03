package com.aimbitious.job;

import com.aimbitious.job.config.AsyncConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@Import(AsyncConfig.class)
public class AimbitiousJobPortalApplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimbitiousJobPortalApplicationServiceApplication.class, args);
	}

}
