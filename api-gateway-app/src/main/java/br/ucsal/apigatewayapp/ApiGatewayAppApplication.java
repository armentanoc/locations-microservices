package br.ucsal.apigatewayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients

public class ApiGatewayAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayAppApplication.class, args);
    }

}
