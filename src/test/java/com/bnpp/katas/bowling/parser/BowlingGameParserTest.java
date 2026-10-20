package com.bnpp.katas.bowling.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

import com.bnpp.katas.bowling.domain.Frame;
import com.bnpp.katas.bowling.domain.Roll;

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
	void parsesConsecutiveStrikesWithoutConsumingFollowingFrames() {
		var parser = new BowlingGameParser();

		var frames = parser.parse("X X 34 -- -- -- -- -- -- --");

		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('X'), new Roll('3'))), frames.get(0));
		assertEquals(new Frame(List.of(new Roll('X')), List.of(new Roll('3'), new Roll('4'))), frames.get(1));
		assertEquals(new Frame(List.of(new Roll('3'), new Roll('4')), List.of()), frames.get(2));
		assertEquals(10, frames.size());
	}

	@Test
	void parsesSpareBonusWithoutConsumingFollowingFrame() {
		var parser = new BowlingGameParser();

		var frames = parser.parse("5/ 34 -- -- -- -- -- -- -- --");

		assertEquals(new Frame(List.of(new Roll('5'), new Roll('/')), List.of(new Roll('3'))), frames.get(0));
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
