package br.ucsal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@FeignClient("users-app")
	static interface NameService {
		@RequestMapping("/")
		public String getName();
	}

}
