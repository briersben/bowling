package com.bnpp.katas.bowling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bnpp.katas.bowling.domain.Frame;
import com.bnpp.katas.bowling.domain.Roll;
import com.bnpp.katas.bowling.parser.BowlingGameParser;

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

	public int countStrikes(String rolls) {
		var frames = parser.parse(rolls);
		var strikesInFrames = frames.stream()
				.flatMap(frame -> frame.rolls().stream())
				.filter(Roll::isStrike)
				.count();
		var strikesInTenthFrameBonus = frames.getLast().bonusRolls().stream()
				.filter(Roll::isStrike)
				.count();
		var totalStrikes = Math.toIntExact(strikesInFrames + strikesInTenthFrameBonus);

		LOGGER.debug("Bowling game strikes: {}", totalStrikes);
		return totalStrikes;
	}

	public int countSpares(String rolls) {
		var totalSpares = Math.toIntExact(parser.parse(rolls).stream()
				.flatMap(frame -> frame.rolls().stream())
				.filter(Roll::isSpare)
				.count());

		LOGGER.debug("Bowling game spares: {}", totalSpares);
		return totalSpares;
	}
}
