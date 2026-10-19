package com.bnpp.katas.bowling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BowlingGameScorer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BowlingGameScorer.class);

	private final BowlingGameRollValidator rollValidator;

	public BowlingGameScorer(BowlingGameRollValidator rollValidator) {
		this.rollValidator = rollValidator;
	}

	public int score(String rolls) {
		var total = rollValidator.validateAndParse(rolls).stream()
				.mapToInt(Frame::score)
				.sum();

		LOGGER.debug("Bowling game score: {}", total);
		return total;
	}
}
