package com.bnpp.katas.bowling;

import java.util.List;

record Frame(List<Roll> rolls, List<Roll> bonusRolls) {

	Frame {
		rolls = List.copyOf(rolls);
		bonusRolls = List.copyOf(bonusRolls);
	}

	int score() {
		var score = 0;
		var previousRollPinCount = 0;

		for (var roll : rolls) {
			previousRollPinCount = roll.pinCount(previousRollPinCount);
			score += previousRollPinCount;
		}

		for (var bonusRoll : bonusRolls) {
			previousRollPinCount = bonusRoll.pinCount(previousRollPinCount);
			score += previousRollPinCount;
		}

		return score;
	}
}
