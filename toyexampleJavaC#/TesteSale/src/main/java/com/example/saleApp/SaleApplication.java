package com.example.saleApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan({"com.elena.application.MsSaleSpring.controller"})



@EnableEurekaClient
@EnableDiscoveryClient
@EnableAutoConfiguration

@EnableFeignClients(basePackages = {"com.elena.application.MsSaleSpring.intercomm", "com.elena.application.MsSaleSpring.controller"})


@SpringBootApplication(scanBasePackages={"com.elena.application.MsSaleSpring.intercomm","com.elena.application.MsSaleSpring.controller"})
public class SaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleApplication.class, args);
	}
	
	 @LoadBalanced
	  @Bean
	  public RestTemplate restTemplate(RestTemplateBuilder builder) {
	     return builder.build();
	  }
}
