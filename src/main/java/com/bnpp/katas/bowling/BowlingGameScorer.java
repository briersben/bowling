package com.bnpp.katas.bowling;

import org.springframework.stereotype.Service;

@Service
public class BowlingGameScorer {

	private final BowlingGameRollValidator rollValidator;

	public BowlingGameScorer(BowlingGameRollValidator rollValidator) {
		this.rollValidator = rollValidator;
	}

	public int score(String rolls) {
		rollValidator.validate(rolls);
		return 0;
	}
}
