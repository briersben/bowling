package com.bnpp.katas.bowling.domain;

public record Roll(char notation) {

	public static final char STRIKE = 'X';
	public static final char SPARE = '/';
	public static final char MISS = '-';

	public boolean isStrike() {
		return notation == STRIKE;
	}

	public boolean isSpare() {
		return notation == SPARE;
	}

	public boolean isMiss() {
		return notation == MISS;
	}

	public boolean isSupportedNotation() {
		return isStrike() || isSpare() || isMiss() || (notation >= '1' && notation <= '9');
	}

	public int pinCount(int previousRollPinCount) {
		return switch (notation) {
			case STRIKE -> 10;
			case MISS -> 0;
			case SPARE -> 10 - previousRollPinCount;
			default -> Character.digit(notation, 10);
		};
	}
}
