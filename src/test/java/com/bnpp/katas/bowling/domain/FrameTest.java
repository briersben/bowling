package com.bnpp.katas.bowling.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class FrameTest {

	@Test
	void scoresStrikeWithTwoBonusRolls() {
		var frame = new Frame(
				List.of(new Roll('X')),
				List.of(new Roll('3'), new Roll('4')));

		assertEquals(17, frame.score());
	}

	@Test
	void scoresSpareWithOneBonusRoll() {
		var frame = new Frame(
				List.of(new Roll('5'), new Roll('/')),
				List.of(new Roll('3')));

		assertEquals(13, frame.score());
	}

}
