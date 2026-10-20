package com.bnpp.katas.bowling.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpp.katas.bowling.BowlingGameScorer;
import com.bnpp.katas.bowling.dto.ScoreRequest;
import com.bnpp.katas.bowling.dto.ScoreResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bowling")
@Tag(name = "Bowling")
public class BowlingController {

	private final BowlingGameScorer scorer;

	public BowlingController(BowlingGameScorer scorer) {
		this.scorer = scorer;
	}

	@Operation(
			summary = "Calculate a bowling game score",
			description = "Scores one complete bowling game from its roll notation.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Score calculated successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ScoreResponse.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid or incomplete roll sequence",
					content = @Content(
							mediaType = "application/problem+json",
							schema = @Schema(implementation = ProblemDetail.class)))
	})
	@PostMapping("/score")
	public ScoreResponse score(@Valid @RequestBody ScoreRequest request) {
		return new ScoreResponse(scorer.score(request.rolls()));
	}
}
