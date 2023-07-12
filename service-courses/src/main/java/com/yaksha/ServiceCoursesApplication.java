package com.yaksha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient // descrubimiento de servicios pero con Kubernetes
@EnableFeignClients
@SpringBootApplication
public class ServiceCoursesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCoursesApplication.class, args);
	}

}
