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
			var firstRoll = normalizedRolls.charAt(rollIndex++);
			if (firstRoll == 'X') {
				total += 10 + nextTwoRollsValue(normalizedRolls, rollIndex);
				continue;
			}

			var secondRoll = normalizedRolls.charAt(rollIndex++);
			if (secondRoll == '/') {
				total += 10 + scoreOfBonusRoll(normalizedRolls, rollIndex);
			} else {
				total += valueOf(firstRoll, 0) + valueOf(secondRoll, 0);
			}
		}

		var tenthFrameFirstRoll = normalizedRolls.charAt(rollIndex++);
		if (tenthFrameFirstRoll == 'X') {
			var secondBonusRoll = normalizedRolls.charAt(rollIndex++);
			var thirdBonusRoll = normalizedRolls.charAt(rollIndex++);
			total += 10 + valueOf(secondBonusRoll, 0) + valueOf(thirdBonusRoll, valueOf(secondBonusRoll, 0));
		} else {
			var secondRoll = normalizedRolls.charAt(rollIndex++);
			if (secondRoll == '/') {
				var bonusRoll = normalizedRolls.charAt(rollIndex++);
				total += 10 + valueOf(bonusRoll, 0);
			} else {
				total += valueOf(tenthFrameFirstRoll, 0) + valueOf(secondRoll, 0);
			}
		}

		LOGGER.debug("Bowling game score: {}", total);
		return total;
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
		if (roll == 'X') {
			return 10;
		}
		if (roll == '-') {
			return 0;
		}
		if (roll == '/') {
			return 10 - previousRollValue;
		}
		return Character.digit(roll, 10);
	}
}
