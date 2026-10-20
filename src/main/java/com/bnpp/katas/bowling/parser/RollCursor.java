package com.bnpp.katas.bowling.parser;

import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bnpp.katas.bowling.domain.Roll;

final class RollCursor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RollCursor.class);

	private final String rolls;
	private int index;

	RollCursor(String rolls) {
		this.rolls = rolls;
	}

	Roll readNextRoll() {
		return readRoll(index++);
	}

	List<Roll> peekNextRolls(int count) {
		var endIndex = index + count;
		return IntStream.range(index, endIndex)
				.mapToObj(this::readRoll)
				.toList();
	}

	List<Roll> readNextRolls(int count) {
		var nextRolls = peekNextRolls(count);
		index += count;
		return nextRolls;
	}

	boolean hasRemainingRolls() {
		return index < rolls.length();
	}

	static IllegalArgumentException invalidRollSequence(String message) {
		LOGGER.warn("Invalid bowling roll sequence: {}", message);
		return new IllegalArgumentException(message);
	}

	private Roll readRoll(int rollIndex) {
		if (rollIndex >= rolls.length()) {
			throw invalidRollSequence("The roll sequence does not complete ten frames");
		}

		var roll = new Roll(rolls.charAt(rollIndex));
		if (!roll.isSupportedNotation()) {
			throw invalidRollSequence("Unsupported roll notation: " + roll.notation());
		}
		return roll;
	}
}
