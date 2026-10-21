package com.bnpp.katas.bowling.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.bnpp.katas.bowling.parser.BowlingGameParser;

@ExtendWith(OutputCaptureExtension.class)
class BowlingGameScorerTest {

	private final BowlingGameScorer scorer = new BowlingGameScorer(new BowlingGameParser());

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
	void rejectsSpareNotationAsFirstRoll() {
		var rolls = "/1 -- -- -- -- -- -- -- -- --";

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
	void scoresConsecutiveStrikesWithCorrectLookahead() {
		var rolls = "X X 34 -- -- -- -- -- -- --";

		assertEquals(47, scorer.score(rolls));
	}

	@Test
	void countsTheTenthFrameSpareBonus() {
		var rolls = "-- -- -- -- -- -- -- -- -- 5/ 3";

		assertEquals(13, scorer.score(rolls));
	}

	@Test
	void countsBothTenthFrameStrikeBonusRolls() {
		var rolls = "-- -- -- -- -- -- -- -- -- X 34";

		assertEquals(17, scorer.score(rolls));
	}

	@Test
	void scoresTenSpareFramesAs150() {
		var rolls = "5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5";

		assertEquals(150, scorer.score(rolls));
	}

	@Test
	void scoresTwelveStrikesAs300() {
		var rolls = "X X X X X X X X X X X X";

		assertEquals(300, scorer.score(rolls));
	}

	@Test
	void countsAllStrikesIncludingTenthFrameBonusRolls() {
		var rolls = "X X X X X X X X X X X X";

		assertEquals(12, scorer.countStrikes(rolls));
	}

	@Test
	void countsAllSpareFramesIncludingTheTenthFrame() {
		var rolls = "5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5";

		assertEquals(10, scorer.countSpares(rolls));
	}

	@Test
	void doesNotCountSpareNotationUsedAsStrikeBonus() {
		var rolls = "5/ 12 12 12 12 12 12 12 12 X 5/";

		assertEquals(1, scorer.countSpares(rolls));
	}

	@Test
	void scoresStrikeSpareAndOpenFrames() {
		var rolls = "X 5/ 34 -- -- -- -- -- -- --";

		assertEquals(40, scorer.score(rolls));
	}

	@Test
	void scoresSpareNotationAsSecondTenthFrameStrikeBonusRoll() {
		var rolls = "-- -- -- -- -- -- -- -- -- X 5/";

		assertEquals(20, scorer.score(rolls));
	}

	@Test
	void logsCalculatedScoreAtDebugLevel(CapturedOutput output) {
		scorer.score("12 12 12 12 12 12 12 12 12 12");

		assertTrue(output.getAll().contains("Bowling game score: 30"));
	}

	@Test
	void logsStrikeCountAtDebugLevel(CapturedOutput output) {
		scorer.countStrikes("X X X X X X X X X X X X");

		assertTrue(output.getAll().contains("Bowling game strikes: 12"));
	}

	@Test
	void logsInvalidRollSequenceWhenGameIsIncomplete(CapturedOutput output) {
		var rolls = "-- -- -- -- -- -- -- -- -- -";

		assertThrows(IllegalArgumentException.class, () -> scorer.score(rolls));
		assertTrue(output.getAll().contains("Invalid bowling roll sequence: The roll sequence does not complete ten frames"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"x x x x x x x x x x x x", "X X X X X X X X X X X X"})
	void acceptsUppercaseAndLowercaseXForStrikeNotation(String rolls) {
		assertDoesNotThrow(() -> scorer.score(rolls));
	}
}
