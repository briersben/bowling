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
