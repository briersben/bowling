package com.bnpp.katas.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class BowlingGameParserTest {

	@Test
	void parsesFramesWithStrikeBonusRolls() {
		var parser = new BowlingGameParser();

		var frames = parser.parse("X 34 -- -- -- -- -- -- -- --");

		assertEquals(10, frames.size());
		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('3'), new Roll('4'))), frames.get(0));
		assertEquals(new Frame(List.of(new Roll('3'), new Roll('4')), List.of()), frames.get(1));
	}

	@Test
	void parsesLowercaseStrikeNotationAsCanonicalFrames() {
		var parser = new BowlingGameParser();

		var frames = parser.parse("x x\tx x x x x x x x x x");

		assertEquals(10, frames.size());
		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('X'), new Roll('X'))), frames.get(0));
		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('X'), new Roll('X'))), frames.getLast());
	}

	@Test
	void parsesSpareBonusAndTenthFrameStrike() {
		var parser = new BowlingGameParser();

		var frames = parser.parse("5/ 34 -- -- -- -- -- -- -- X 34");

		assertEquals(10, frames.size());
		assertEquals(new Frame(List.of(new Roll('5'), new Roll('/')), List.of(new Roll('3'))), frames.get(0));
		assertEquals(new Frame(List.of(new Roll('3'), new Roll('4')), List.of()), frames.get(1));
		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('3'), new Roll('4'))), frames.getLast());
	}
}
