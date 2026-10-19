package com.bnpp.katas.bowling;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BowlingGameRollValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(BowlingGameRollValidator.class);
	private static final char STRIKE = 'X';
	private static final char SPARE = '/';

	public List<Frame> validateAndParse(String rolls) {
		var normalizedRolls = rolls.replaceAll("\\s", "").toUpperCase(Locale.ROOT);
		var rollCursor = new RollCursor(normalizedRolls);
		var frames = new ArrayList<Frame>(10);

		for (var frame = 0; frame < 9; frame++) {
			frames.add(parseRegularFrame(rollCursor));
		}

		frames.add(parseTenthFrame(rollCursor));

		if (rollCursor.hasRemainingRolls()) {
			throw invalidRollSequence("A game must contain exactly ten frames");
		}

		return List.copyOf(frames);
	}

	private Frame parseRegularFrame(RollCursor rollCursor) {
		var firstRoll = rollCursor.readNextRoll();
		if (isStrike(firstRoll)) {
			return new Frame(
					String.valueOf(firstRoll),
					rollCursor.peekNextRolls(2));
		}

		var secondRoll = rollCursor.readNextRoll();
		var frameRolls = "" + firstRoll + secondRoll;
		var bonusRolls = isSpare(secondRoll) ? rollCursor.peekNextRolls(1) : "";
		return new Frame(frameRolls, bonusRolls);
	}

	private Frame parseTenthFrame(RollCursor rollCursor) {
		var firstRoll = rollCursor.readNextRoll();
		if (isStrike(firstRoll)) {
			return new Frame(
					String.valueOf(firstRoll),
					rollCursor.readNextRolls(2));
		}

		var secondRoll = rollCursor.readNextRoll();
		var frameRolls = "" + firstRoll + secondRoll;
		var bonusRolls = isSpare(secondRoll) ? rollCursor.readNextRolls(1) : "";
		return new Frame(frameRolls, bonusRolls);
	}

	private char readRoll(String normalizedRolls, int index) {
		if (index >= normalizedRolls.length()) {
			throw invalidRollSequence("The roll sequence does not complete ten frames");
		}

		var roll = normalizedRolls.charAt(index);
		if (!isStrike(roll) && !isSpare(roll) && roll != '-' && (roll < '1' || roll > '9')) {
			throw invalidRollSequence("Unsupported roll notation: " + roll);
		}
		return roll;
	}

	private IllegalArgumentException invalidRollSequence(String message) {
		LOGGER.warn("Invalid bowling roll sequence: {}", message);
		return new IllegalArgumentException(message);
	}

	private boolean isStrike(char roll) {
		return roll == STRIKE;
	}

	private boolean isSpare(char roll) {
		return roll == SPARE;
	}

	private final class RollCursor {

		private final String rolls;
		private int index;

		private RollCursor(String rolls) {
			this.rolls = rolls;
		}

		private char readNextRoll() {
			return readRoll(rolls, index++);
		}

		private String peekNextRolls(int count) {
			var endIndex = index + count;
			for (var rollIndex = index; rollIndex < endIndex; rollIndex++) {
				readRoll(rolls, rollIndex);
			}
			return rolls.substring(index, endIndex);
		}

		private String readNextRolls(int count) {
			var nextRolls = peekNextRolls(count);
			index += count;
			return nextRolls;
		}

		private boolean hasRemainingRolls() {
			return index < rolls.length();
		}
	}
}
