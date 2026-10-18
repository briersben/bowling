package com.bnpp.katas.bowling;

import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class BowlingGameRollValidator {

	private static final char STRIKE = 'X';
	private static final char SPARE = '/';

	public void validate(String rolls) {
		var normalizedRolls = rolls.replaceAll("\\s", "").toUpperCase(Locale.ROOT);
		var rollIndex = 0;

		for (var frame = 0; frame < 9; frame++) {
			if (!isStrike(readRoll(normalizedRolls, rollIndex++))) {
				readRoll(normalizedRolls, rollIndex++);
			}
		}

		var firstRollInTenthFrame = readRoll(normalizedRolls, rollIndex++);
		if (isStrike(firstRollInTenthFrame)) {
			readRoll(normalizedRolls, rollIndex++);
			readRoll(normalizedRolls, rollIndex++);
		} else if (isSpare(readRoll(normalizedRolls, rollIndex++))) {
			readRoll(normalizedRolls, rollIndex++);
		}

		if (rollIndex != normalizedRolls.length()) {
			throw new IllegalArgumentException("A game must contain exactly ten frames");
		}
	}

	private char readRoll(String normalizedRolls, int index) {
		if (index >= normalizedRolls.length()) {
			throw new IllegalArgumentException("The roll sequence does not complete ten frames");
		}

		var roll = normalizedRolls.charAt(index);
		if (!isStrike(roll) && !isSpare(roll) && roll != '-' && (roll < '1' || roll > '9')) {
			throw new IllegalArgumentException("Unsupported roll notation: " + roll);
		}
		return roll;
	}

	private boolean isStrike(char roll) {
		return roll == STRIKE;
	}

	private boolean isSpare(char roll) {
		return roll == SPARE;
	}
}
