package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.entities")
@ComponentScan({"com.controller"})
@EnableJpaRepositories("com.DAO")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableFeignClients(basePackages = {"com.controller"})
@SpringBootApplication(scanBasePackages={"com.DAO","com.entities","com.controller"})

public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class,args);
    }
}
