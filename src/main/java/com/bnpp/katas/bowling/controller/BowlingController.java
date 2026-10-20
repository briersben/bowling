package com.bnpp.katas.bowling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bnpp.katas.bowling.api.BowlingApi;
import com.bnpp.katas.bowling.dto.ScoreRequest;
import com.bnpp.katas.bowling.dto.ScoreResponse;
import com.bnpp.katas.bowling.service.BowlingGameScorer;

@RestController
public class BowlingController implements BowlingApi {

	private final BowlingGameScorer scorer;

	public BowlingController(BowlingGameScorer scorer) {
		this.scorer = scorer;
	}

	@Override
	public ScoreResponse scoreBowlingGame(ScoreRequest scoreRequest) {
		return new ScoreResponse(scorer.score(scoreRequest.getRolls()));
	}
}
