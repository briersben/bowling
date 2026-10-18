package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BowlingGameScorerTest {

	private final BowlingGameScorer scorer = new BowlingGameScorer(new BowlingGameRollValidator());

	@Test
	void rejectsTooFewRollsToCompleteTenFrames() {
		var rolls = "-- -- -- -- -- -- -- -- -- -";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
	}

	@Test
	void rejectsTenthFrameSpareWithoutBonusRoll() {
		var rolls = "-- -- -- -- -- -- -- -- -- 5/";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
	}

	@Test
	void acceptsLowercaseXForStrikeNotation() {
		var rolls = "x x x x x x x x x x x x";

		assertDoesNotThrow(() -> scorer.score(rolls));
	}

	@Test
	void acceptsUppercaseXForStrikeNotation() {
		var rolls = "X X X X X X X X X X X X";

		assertDoesNotThrow(() -> scorer.score(rolls));
	}
}
