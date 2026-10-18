package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class BowlingGameScorerTest {

	private final BowlingGameScorer scorer = new BowlingGameScorer();

	@Test
	void rejectsTooFewRollsToCompleteTenFrames() {
		var rolls = Collections.nCopies(19, 0);

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
	}
}
