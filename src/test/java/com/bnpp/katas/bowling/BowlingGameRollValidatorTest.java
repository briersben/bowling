package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BowlingGameRollValidatorTest {

	@Test
	void parsesFramesWithStrikeBonusRolls() {
		var validator = new BowlingGameRollValidator();

		var frames = validator.validateAndParse("X 34 -- -- -- -- -- -- -- --");

		assertEquals(10, frames.size());
		assertEquals(new Frame("X", "34"), frames.get(0));
		assertEquals(new Frame("34", ""), frames.get(1));
	}

	@Test
	void parsesLowercaseStrikeNotationAsCanonicalFrames() {
		var validator = new BowlingGameRollValidator();

		var frames = validator.validateAndParse("x x\tx x x x x x x x x x");

		assertEquals(10, frames.size());
		assertEquals(new Frame("X", "XX"), frames.get(0));
		assertEquals(new Frame("X", "XX"), frames.getLast());
	}

	@Test
	void parsesSpareBonusAndTenthFrameStrike() {
		var validator = new BowlingGameRollValidator();

		var frames = validator.validateAndParse("5/ 34 -- -- -- -- -- -- -- X 34");

		assertEquals(10, frames.size());
		assertEquals(new Frame("5/", "3"), frames.get(0));
		assertEquals(new Frame("34", ""), frames.get(1));
		assertEquals(new Frame("X", "34"), frames.getLast());
	}
}
