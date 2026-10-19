package com.bnpp.katas.bowling;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void publishesBowlingScoreEndpointInOpenApiDocument() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("Bowling API"))
				.andExpect(jsonPath("$.info.version").value("1.0"))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.summary")
						.value("Calculate a bowling game score"))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.description")
						.value("Scores one complete bowling game from its roll notation."))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.requestBody.content['application/json'].schema.$ref")
						.value("#/components/schemas/ScoreRequest"))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.responses['200'].description")
						.value("Score calculated successfully"))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.responses['200'].content['application/json'].schema.$ref")
						.value("#/components/schemas/ScoreResponse"))
				.andExpect(jsonPath("$.paths['/api/bowling/score'].post.responses['400'].description")
						.value("Invalid or incomplete roll sequence"));
	}

	@Test
	void servesSwaggerUi() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());
	}
}
