package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RollTest {

	@Test
	void scoresStrikeAsTenPins() {
		assertEquals(10, new Roll('X').pinCount(0));
	}

	@Test
	void scoresMissAndDigitNotation() {
		assertEquals(0, new Roll('-').pinCount(0));
		assertEquals(7, new Roll('7').pinCount(0));
	}

	@Test
	void scoresSpareAsPinsNeededAfterPreviousRoll() {
		assertEquals(5, new Roll('/').pinCount(5));
	}

	@Test
	void identifiesStrikeSpareAndSupportedNotation() {
		assertTrue(new Roll(Roll.STRIKE).isStrike());
		assertFalse(new Roll('5').isStrike());
		assertTrue(new Roll(Roll.SPARE).isSpare());
		assertFalse(new Roll('5').isSpare());
		assertTrue(new Roll('X').isSupportedNotation());
		assertTrue(new Roll('/').isSupportedNotation());
		assertTrue(new Roll(Roll.MISS).isMiss());
		assertFalse(new Roll('5').isMiss());
		assertTrue(new Roll(Roll.MISS).isSupportedNotation());
		assertTrue(new Roll('5').isSupportedNotation());
		assertFalse(new Roll('?').isSupportedNotation());
	}
}
