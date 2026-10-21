package com.bnpp.katas.bowling.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;
import org.springframework.stereotype.Component;

import com.bnpp.katas.bowling.domain.Frame;
import com.bnpp.katas.bowling.domain.Roll;

@Component
public class BowlingGameParser {

	public List<Frame> parse(String rolls) {
		var normalizedRolls = rolls.replaceAll("\\s", "").toUpperCase(Locale.ROOT);
		var rollCursor = new RollCursor(normalizedRolls);
		var frames = new ArrayList<Frame>(10);

		// A loop keeps the nine regular frames separate from the tenth; a stream would not simplify its bonus-roll handling.
		for (var frame = 0; frame < 9; frame++) {
			frames.add(parseRegularFrame(rollCursor));
		}

		frames.add(parseTenthFrame(rollCursor));

		if (rollCursor.hasRemainingRolls()) {
			throw RollCursor.invalidRollSequence("A game must contain exactly ten frames");
		}

		return List.copyOf(frames);
	}

	private Frame parseRegularFrame(RollCursor rollCursor) {
		return parseFrame(rollCursor, rollCursor::peekNextRolls);
	}

	private Frame parseTenthFrame(RollCursor rollCursor) {
		return parseFrame(rollCursor, rollCursor::readNextRolls);
	}

	private Frame parseFrame(RollCursor rollCursor, IntFunction<List<Roll>> bonusRollReader) {
		var firstRoll = parseFirstRoll(rollCursor);
		if (firstRoll.isStrike()) {
			return new Frame(List.of(firstRoll), bonusRollReader.apply(2));
		}

		return parseSecondRoll(rollCursor, firstRoll, bonusRollReader);
	}

	private Roll parseFirstRoll(RollCursor rollCursor) {
		var firstRoll = rollCursor.readNextRoll();
		if (firstRoll.isSpare()) {
			throw RollCursor.invalidRollSequence("A spare cannot be the first roll of a frame");
		}
		return firstRoll;
	}

	private Frame parseSecondRoll(RollCursor rollCursor, Roll firstRoll,
			IntFunction<List<Roll>> bonusRollReader) {
		var secondRoll = rollCursor.readNextRoll();
		var frameRolls = List.of(firstRoll, secondRoll);
		var bonusRolls = secondRoll.isSpare() ? bonusRollReader.apply(1) : List.<Roll>of();
		return new Frame(frameRolls, bonusRolls);
	}
}
