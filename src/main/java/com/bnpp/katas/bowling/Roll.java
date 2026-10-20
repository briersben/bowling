package com.bnpp.katas.bowling;

record Roll(char notation) {

	static final char STRIKE = 'X';
	static final char SPARE = '/';
	static final char MISS = '-';

	boolean isStrike() {
		return notation == STRIKE;
	}

	boolean isSpare() {
		return notation == SPARE;
	}

	boolean isMiss() {
		return notation == MISS;
	}

	boolean isSupportedNotation() {
		return isStrike() || isSpare() || isMiss() || (notation >= '1' && notation <= '9');
	}

	int pinCount(int previousRollPinCount) {
		return switch (notation) {
			case STRIKE -> 10;
			case MISS -> 0;
			case SPARE -> 10 - previousRollPinCount;
			default -> Character.digit(notation, 10);
		};
	}
}
