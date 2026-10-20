package com.bnpp.katas.bowling;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BowlingGameParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(BowlingGameParser.class);

	public List<Frame> parse(String rolls) {
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
		if (firstRoll.isStrike()) {
			return new Frame(
					List.of(firstRoll),
					rollCursor.peekNextRolls(2));
		}

		var secondRoll = rollCursor.readNextRoll();
		var frameRolls = List.of(firstRoll, secondRoll);
		var bonusRolls = secondRoll.isSpare() ? rollCursor.peekNextRolls(1) : List.<Roll>of();
		return new Frame(frameRolls, bonusRolls);
	}

	private Frame parseTenthFrame(RollCursor rollCursor) {
		var firstRoll = rollCursor.readNextRoll();
		if (firstRoll.isStrike()) {
			return new Frame(
					List.of(firstRoll),
					rollCursor.readNextRolls(2));
		}

		var secondRoll = rollCursor.readNextRoll();
		var frameRolls = List.of(firstRoll, secondRoll);
		var bonusRolls = secondRoll.isSpare() ? rollCursor.readNextRolls(1) : List.<Roll>of();
		return new Frame(frameRolls, bonusRolls);
	}

	private Roll readRoll(String normalizedRolls, int index) {
		if (index >= normalizedRolls.length()) {
			throw invalidRollSequence("The roll sequence does not complete ten frames");
		}

		var roll = new Roll(normalizedRolls.charAt(index));
		if (!roll.isSupportedNotation()) {
			throw invalidRollSequence("Unsupported roll notation: " + roll.notation());
		}
		return roll;
	}

	private IllegalArgumentException invalidRollSequence(String message) {
		LOGGER.warn("Invalid bowling roll sequence: {}", message);
		return new IllegalArgumentException(message);
	}

	private final class RollCursor {

		private final String rolls;
		private int index;

		private RollCursor(String rolls) {
			this.rolls = rolls;
		}

		private Roll readNextRoll() {
			return readRoll(rolls, index++);
		}

		private List<Roll> peekNextRolls(int count) {
			var endIndex = index + count;
			var nextRolls = new ArrayList<Roll>(count);
			for (var rollIndex = index; rollIndex < endIndex; rollIndex++) {
				nextRolls.add(readRoll(rolls, rollIndex));
			}
			return List.copyOf(nextRolls);
		}

		private List<Roll> readNextRolls(int count) {
			var nextRolls = peekNextRolls(count);
			index += count;
			return nextRolls;
		}

		private boolean hasRemainingRolls() {
			return index < rolls.length();
		}
	}
}
