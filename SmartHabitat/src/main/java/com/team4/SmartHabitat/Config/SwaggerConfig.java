package com.team4.SmartHabitat.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customConfig() {
		return new OpenAPI().info(new Info().title("Smart Habitat System").description("By Team4")).servers(List.of(new Server().url("http://localhost:3001").description("local"),
				new Server().url("http://localhost:3001").description("live")
				));
	}
}
