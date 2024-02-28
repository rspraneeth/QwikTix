package com.qwiktix;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Event booking API", version = "1.1",
		description = "Event booking API"),
		servers = @Server(
				url = "http://localhost:9090",
				description = "This API url will help in booking an event."
		)
)
public class QwikTixApplication {

	public static void main(String[] args) {
		SpringApplication.run(QwikTixApplication.class, args);
	}

}
