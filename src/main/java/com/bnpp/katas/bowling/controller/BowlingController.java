package com.bnpp.katas.bowling.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bnpp.katas.bowling.BowlingGameScorer;
import com.bnpp.katas.bowling.dto.ScoreRequest;
import com.bnpp.katas.bowling.dto.ScoreResponse;

import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "Calculate a bowling game score")
	@PostMapping("/score")
	public ScoreResponse score(@Valid @RequestBody ScoreRequest request) {
		return new ScoreResponse(scorer.score(request.rolls()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProblemDetail handleInvalidRollSequence(IllegalArgumentException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
}
