package com.bnpp.katas.bowling;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
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
	void servesOpenApiContract() throws Exception {
		mockMvc.perform(get("/openapi/bowling.yaml"))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("openapi: 3.0.3")))
				.andExpect(content().string(Matchers.containsString("/api/bowling/score:")))
				.andExpect(content().string(Matchers.containsString("/api/bowling/strikes:")))
				.andExpect(content().string(Matchers.containsString("/api/bowling/spares:")))
				.andExpect(content().string(Matchers.containsString("Calculate a bowling game score")))
				.andExpect(content().string(Matchers.containsString("Count strikes in a bowling game")))
				.andExpect(content().string(Matchers.containsString("Count spares in a bowling game")))
				.andExpect(content().string(Matchers.containsString("Invalid or incomplete roll sequence")));
	}

	@Test
	void servesSwaggerUi() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());
	}
}
