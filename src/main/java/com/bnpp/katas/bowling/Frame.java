package com.bnpp.katas.bowling;

record Frame(String rolls, String bonusRolls) {

	int score() {
		var allRolls = rolls + bonusRolls;
		var score = 0;
		var previousRollPinCount = 0;

		for (var index = 0; index < allRolls.length(); index++) {
			previousRollPinCount = valueOf(allRolls.charAt(index), previousRollPinCount);
			score += previousRollPinCount;
		}
		return score;
	}

	private int valueOf(char roll, int previousRollPinCount) {
		return switch (roll) {
			case 'X' -> 10;
			case '-' -> 0;
			case '/' -> 10 - previousRollPinCount;
			default -> Character.digit(roll, 10);
		};
	}
}
