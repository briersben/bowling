package com.bnpp.katas.bowling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BowlingGameScorer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BowlingGameScorer.class);

	private final BowlingGameParser parser;

	public BowlingGameScorer(BowlingGameParser parser) {
		this.parser = parser;
	}

	public int score(String rolls) {
		var total = parser.parse(rolls).stream()
				.mapToInt(Frame::score)
				.sum();

		LOGGER.debug("Bowling game score: {}", total);
		return total;
	}
}
