package com.bnpp.katas.bowling.domain;

import java.util.List;

public record Frame(List<Roll> rolls, List<Roll> bonusRolls) {

	public Frame {
		rolls = List.copyOf(rolls);
		bonusRolls = List.copyOf(bonusRolls);
	}

	public int score() {
		var score = 0;
		var previousRollPinCount = 0;

		// A spare's value depends on the preceding roll.
		for (var roll : rolls) {
			previousRollPinCount = roll.pinCount(previousRollPinCount);
			score += previousRollPinCount;
		}

		// Bonus rolls use the last frame roll when calculating spare values.
		for (var bonusRoll : bonusRolls) {
			previousRollPinCount = bonusRoll.pinCount(previousRollPinCount);
			score += previousRollPinCount;
		}

		return score;
	}
}
