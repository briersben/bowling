package com.bnpp.katas.bowling.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bnpp.katas.bowling.service.BowlingGameScorer;

@WebMvcTest(BowlingController.class)
class BowlingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BowlingGameScorer scorer;

	@Test
	void scoresRollSequence() throws Exception {
		var rolls = "--------------------";
		when(scorer.score(rolls)).thenReturn(42);

		mockMvc.perform(post("/api/bowling/score")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"rolls":"--------------------"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.score").value(42));

		verify(scorer).score(rolls);
	}

	@Test
	void countsStrikesInRollSequence() throws Exception {
		var rolls = "X X X X X X X X X X X X";
		when(scorer.countStrikes(rolls)).thenReturn(12);

		mockMvc.perform(post("/api/bowling/strikes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"rolls":"X X X X X X X X X X X X"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value(12));

		verify(scorer).countStrikes(rolls);
	}

	@Test
	void countsSparesInRollSequence() throws Exception {
		var rolls = "5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5";
		when(scorer.countSpares(rolls)).thenReturn(10);

		mockMvc.perform(post("/api/bowling/spares")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"rolls":"5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value(10));

		verify(scorer).countSpares(rolls);
	}

	@Test
	void rejectsBlankRollSequence() throws Exception {
		mockMvc.perform(post("/api/bowling/score")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"rolls":" "}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void returnsBadRequestForInvalidRollSequence() throws Exception {
		when(scorer.score("invalid")).thenThrow(new IllegalArgumentException("Unsupported roll notation: I"));

		mockMvc.perform(post("/api/bowling/score")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"rolls":"invalid"}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.detail").value("Unsupported roll notation: I"));
	}
}
