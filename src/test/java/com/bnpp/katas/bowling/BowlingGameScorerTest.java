package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
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
	void rejectsTenthFrameStrikeWithoutBothBonusRolls() {
		var rolls = "-- -- -- -- -- -- -- -- -- X 3";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
	}

	@Test
	void rejectsExtraRollsAfterTenFrames() {
		var rolls = "12 12 12 12 12 12 12 12 12 12 1";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
	}

	@Test
	void scoresTenOpenFrames() {
		var rolls = "12 12 12 12 12 12 12 12 12 12";

		assertEquals(30, scorer.score(rolls));
	}

	@Test
	void scoresTenNineAndMissFrames() {
		var rolls = "9- 9- 9- 9- 9- 9- 9- 9- 9- 9-";

		assertEquals(90, scorer.score(rolls));
	}

	@Test
	void scoresGutterGameAsZero() {
		var rolls = "-- -- -- -- -- -- -- -- -- --";

		assertEquals(0, scorer.score(rolls));
	}

	@Test
	void addsTheNextRollToASpare() {
		var rolls = "5/ 3- -- -- -- -- -- -- -- --";

		assertEquals(16, scorer.score(rolls));
	}

	@Test
	void addsTheNextTwoRollsToAStrike() {
		var rolls = "X 34 -- -- -- -- -- -- -- --";

		assertEquals(24, scorer.score(rolls));
	}

	@Test
	void scoresStrikeSpareAndOpenFrames() {
		var rolls = "X 5/ 3 4 -- -- -- -- -- -- --";

		assertEquals(40, scorer.score(rolls));
	}

	@Test
	void scoresTenthFrameSpareAndBonusRoll() {
		var rolls = "-- -- -- -- -- -- -- -- -- 5/3";

		assertEquals(13, scorer.score(rolls));
	}

	@Test
	void scoresTenthFrameStrikeAndBothBonusRolls() {
		var rolls = "-- -- -- -- -- -- -- -- -- X34";

		assertEquals(17, scorer.score(rolls));
	}

	@Test
	void scoresSpareNotationAsSecondTenthFrameStrikeBonusRoll() {
		var rolls = "-- -- -- -- -- -- -- -- -- X5/";

		assertEquals(20, scorer.score(rolls));
	}

	@Test
	void validatesAndReturnsCanonicalRolls() {
		var validator = new BowlingGameRollValidator();

		assertEquals("XXXXXXXXXXXX", validator.validateAndNormalize("x x\tx x x x x x x x x x"));
	}

	@Test
	void logsCalculatedScoreAtDebugLevel(CapturedOutput output) {
		scorer.score("12 12 12 12 12 12 12 12 12 12");

		assertTrue(output.getAll().contains("Bowling game score: 30"));
	}

	@Test
	void logsInvalidRollSequenceWhenGameIsIncomplete(CapturedOutput output) {
		var rolls = "-- -- -- -- -- -- -- -- -- -";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
		assertTrue(output.getAll().contains("Invalid bowling roll sequence: The roll sequence does not complete ten frames"));
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
