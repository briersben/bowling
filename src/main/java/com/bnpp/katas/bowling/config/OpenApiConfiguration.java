package com.bnpp.katas.bowling.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
		title = "Bowling API",
		version = "1.0",
		description = "REST API for calculating the total score of a ten-pin bowling game."))
public class OpenApiConfiguration {
}
