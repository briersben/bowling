package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FrameTest {

	@Test
	void scoresStrikeWithTwoBonusRolls() {
		var frame = new Frame("X", "34");

		assertEquals(17, frame.score());
	}

	@Test
	void scoresSpareWithOneBonusRoll() {
		var frame = new Frame("5/", "3");

		assertEquals(13, frame.score());
	}
}
