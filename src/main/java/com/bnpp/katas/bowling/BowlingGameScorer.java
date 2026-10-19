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
		var normalizedRolls = rollValidator.validateAndNormalize(rolls);
		var total = 0;
		var rollIndex = 0;

		for (var frame = 0; frame < 9; frame++) {
			var frameScore = scoreRegularFrame(normalizedRolls, rollIndex);
			total += frameScore.points();
			rollIndex += frameScore.rollsConsumed();
		}

		total += scoreTenthFrame(normalizedRolls, rollIndex);

		LOGGER.debug("Bowling game score: {}", total);
		return total;
	}

	private int scoreTenthFrame(String rolls, int rollIndex) {
		var firstRoll = rolls.charAt(rollIndex++);
		if (firstRoll == 'X') {
			return 10 + nextTwoRollsValue(rolls, rollIndex);
		}

		var secondRoll = rolls.charAt(rollIndex++);
		if (secondRoll == '/') {
			return 10 + scoreOfBonusRoll(rolls, rollIndex);
		}
		return valueOf(firstRoll, 0) + valueOf(secondRoll, 0);
	}

	private FrameScore scoreRegularFrame(String rolls, int rollIndex) {
		var firstRoll = rolls.charAt(rollIndex);
		if (firstRoll == 'X') {
			return new FrameScore(10 + nextTwoRollsValue(rolls, rollIndex + 1), 1);
		}

		var secondRoll = rolls.charAt(rollIndex + 1);
		if (secondRoll == '/') {
			return new FrameScore(10 + scoreOfBonusRoll(rolls, rollIndex + 2), 2);
		}

		return new FrameScore(valueOf(firstRoll, 0) + valueOf(secondRoll, 0), 2);
	}

	private int nextTwoRollsValue(String normalizedRolls, int startIndex) {
		var firstBonusRoll = normalizedRolls.charAt(startIndex++);
		var secondBonusRoll = normalizedRolls.charAt(startIndex);
		return valueOf(firstBonusRoll, 0) + valueOf(secondBonusRoll, valueOf(firstBonusRoll, 0));
	}

	private int scoreOfBonusRoll(String normalizedRolls, int startIndex) {
		return valueOf(normalizedRolls.charAt(startIndex), 0);
	}

	private int valueOf(char roll, int previousRollValue) {
		return switch (roll) {
			case 'X' -> 10;
			case '-' -> 0;
			case '/' -> 10 - previousRollValue;
			default -> Character.digit(roll, 10);
		};
	}

	private record FrameScore(int points, int rollsConsumed) {
	}
}
