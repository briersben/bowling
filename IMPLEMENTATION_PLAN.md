# Bowling Scorer Implementation Plan

## Goal and scope

Implement a scorer for American ten-pin bowling that returns the total score
only. Validate that the roll sequence can represent exactly ten complete
frames, including any bonus rolls required in the tenth frame. Reject a roll
sequence with too few or extra rolls using `IllegalArgumentException`.
Validating pin counts, roll values, and intermediate frame scores remains out
of scope.

The scorer accepts a notation `String`: `X` or `x` means strike, `1`-`9` are
pin counts, `-` is a miss, and `/` marks a spare. Whitespace is ignored.
`BowlingGameScorer.score(String rolls)` returns the total for a complete game.

The planned roll-count-validation and scoring tests are implemented and
passing, and all numbered steps below are marked DONE. Scoring is implemented;
the test sequence is retained as a record of the completed TDD workflow.

The scorer belongs in `com.bnpp.katas.bowling` and should be a Spring-managed
service (`@Service`). Keep notation parsing and roll-count validation in a
separate `BowlingGameParser` component, injected into the scorer through its
constructor. Inject the scorer through constructors into any other
Spring-managed consumers. Keep focused scorer tests as ordinary JUnit tests
without starting Spring; construct the real parser for scorer tests and
use Mockito only if a test needs to isolate a real collaborator. Put tests in
`src/test/java/com/bnpp/katas/bowling/BowlingGameScorerTest.java`.

## TDD checkpoint protocol

Complete one numbered test at a time. Do not add the later tests in advance.

1. Add only the current test, with a descriptive name, the rolls in the plan,
   and its expected total.
2. Run only that test from the repository root:
   `.\mvnw.cmd "-Dtest=BowlingGameScorerTest#<testMethod>" test`
3. Stop at the first failure and show the user the test and the relevant
   failure output. Wait for the user's explicit go-ahead before editing
   production code or moving on.
4. After approval, make the smallest production change that makes the current
   test pass. Rerun that test, report the green result, and wait for approval
   before adding the next test.
5. After all planned cases pass, run the full suite with
   `.\mvnw.cmd test`. Refactoring is optional and requires the user's approval;
   rerun the suite after approved refactoring.

Use the Progress tracking table as the source of truth when resuming work.
Continue at the earliest `PENDING` step; if a step is `RED — awaiting review`,
wait for the user's approval before changing production code. Do not implement
scoring as part of setting up a test. If a focused test fails to compile for an
API/setup reason, report that separately from a meaningful failing assertion
and stop for review.

If any test fails due to an unrelated build, environment, or test-discovery
problem, report that separately and resolve it with the user; do not call it a
meaningful red step.

## Progress tracking

Status meanings:

- **DONE**: the test and behavior are implemented, and the focused test passes.
- **RED — awaiting review**: the new test is present and has failed as
  expected; production code must not be changed until the user approves.
- **PENDING**: not started.
- **BLOCKED**: progress cannot continue; explain the reason beside the status.

| Step | Test | Status |
| ---: | --- | --- |
| 1 | `rejectsTooFewRollsToCompleteTenFrames` | DONE |
| 2 | `rejectsTenthFrameSpareWithoutBonusRoll` | DONE |
| 3 | `rejectsTenthFrameStrikeWithoutBothBonusRolls` | DONE |
| 4 | `rejectsExtraRollsAfterTenFrames` | DONE |
| 5 | `scoresTenOpenFrames` | DONE |
| 6 | `scoresGutterGameAsZero` | DONE |
| 7 | `scoresTenNineAndMissFrames` | DONE |
| 8 | `addsTheNextRollToASpare` | DONE |
| 9 | `addsTheNextTwoRollsToAStrike` | DONE |
| 10 | `scoresConsecutiveStrikesWithCorrectLookahead` | DONE |
| 11 | `countsTheTenthFrameSpareBonus` | DONE |
| 12 | `countsBothTenthFrameStrikeBonusRolls` | DONE |
| 13 | `scoresTenSpareFramesAs150` | DONE |
| 14 | `scoresTwelveStrikesAs300` | DONE |

Notation acceptance checks for uppercase `X` and lowercase `x` are also
implemented and passing; they are supporting format checks, not separate
numbered plan steps.

## Test sequence

A strike (`X` or `x`) ends its frame after one roll. A slash (`/`) is the
second roll of a spare frame and represents the pins needed to reach ten. A
dash (`-`) means zero pins; digits `1`-`9` give the pins knocked down.
Whitespace is ignored. When a sequence says “repeat,” repeat the notation
exactly the stated number of times. Each numbered case is a separate Red-Green
cycle. The invalid sequences in the first phase intentionally test roll-count
validation.

### Phase 1: Validate that the sequence completes exactly ten frames

Run these count-validation cycles before adding scoring tests. After each
test, stop at red for review, then make the smallest approved change and stop
again at green.

#### 1. Reject too few rolls to complete ten frames

- Suggested test name: `rejectsTooFewRollsToCompleteTenFrames`
- Rolls: `"-- -- -- -- -- -- -- -- -- -"` (nine gutter frames and one roll in
  the tenth frame; 19 rolls total).
- Expected result: throw `IllegalArgumentException`.
- Check: nineteen gutter rolls form only nine complete two-roll frames and
  leave the tenth frame incomplete.
- Purpose: establish incomplete regular-frame detection before scoring cases.

#### 2. Reject a tenth-frame spare without its bonus roll

- Suggested test name: `rejectsTenthFrameSpareWithoutBonusRoll`
- Rolls: `"-- -- -- -- -- -- -- -- -- 5/"` (nine gutter frames and an
  incomplete tenth-frame spare).
- Expected result: throw `IllegalArgumentException`.
- Check: the tenth-frame spare requires exactly one bonus roll.
- Purpose: verify that a spare's required tenth-frame bonus is part of a
  complete game.

#### 3. Reject a tenth-frame strike without both bonus rolls

- Suggested test name: `rejectsTenthFrameStrikeWithoutBothBonusRolls`
- Rolls: `"-- -- -- -- -- -- -- -- -- X3"` (nine gutter frames and a
  tenth-frame strike with one bonus roll).
- Expected result: throw `IllegalArgumentException`.
- Check: the tenth-frame strike requires two bonus rolls; only one is present.
- Purpose: verify that the tenth-frame strike cannot finish with only one
  bonus roll.

#### 4. Reject extra rolls after ten frames

- Suggested test name: `rejectsExtraRollsAfterTenFrames`
- Rolls: `"12 12 12 12 12 12 12 12 12 12 1"` (ten open frames followed by
  one extra roll; 21 rolls total).
- Expected result: throw `IllegalArgumentException`.
- Check: the ten open frames consume exactly 20 rolls; the twenty-first roll
  cannot be assigned to a frame or a tenth-frame bonus.
- Purpose: ensure the scorer does not silently ignore extra rolls.

### Phase 2: Score complete valid games

All sequences in this phase are complete, valid games.

#### 5. Sum a basic open game

- Suggested test name: `scoresTenOpenFrames`
- Rolls: `"12 12 12 12 12 12 12 12 12 12"`.
- Expected total: `30`.
- Check: ten frames score `1 + 2 = 3` each; `10 × 3 = 30`.
- Purpose: establishes the total-score API and ordinary two-roll frame
  accumulation. Add it only after the roll-count validation phase is
  complete.

#### 6. Score a gutter game

- Suggested test name: `scoresGutterGameAsZero`
- Rolls: `"-- -- -- -- -- -- -- -- -- --"`.
- Expected total: `0`.
- Check: all twenty rolls knock down zero pins.
- Purpose: verifies the zero-score boundary after basic accumulation works.

#### 7. Cover the README's open-frame example

- Suggested test name: `scoresTenNineAndMissFrames`
- Rolls: `"9- 9- 9- 9- 9- 9- 9- 9- 9- 9-"`.
- Expected total: `90`.
- Check: ten open frames score `9` each; `10 × 9 = 90`.
- Purpose: confirms the kata's all-open example.

#### 8. Add a spare bonus

- Suggested test name: `addsTheNextRollToASpare`
- Rolls: `"5/ 3- -- -- -- -- -- -- -- --"`.
- Expected total: `16`.
- Check: the spare frame scores `10 + 3 = 13`; the following frame scores
  `3`; the remaining frames score zero. Total: `13 + 3 = 16`.
- Purpose: verifies a spare uses exactly the next roll as its bonus.

#### 9. Add a strike bonus

- Suggested test name: `addsTheNextTwoRollsToAStrike`
- Rolls: `"X 34 -- -- -- -- -- -- -- --"`.
- Expected total: `24`.
- Check: the strike frame scores `10 + 3 + 4 = 17`; the next frame scores
  `3 + 4 = 7`; the remaining frames score zero. Total: `17 + 7 = 24`.
- Purpose: verifies that a strike ends its frame after one roll and receives
  the next two rolls as a bonus.

#### 10. Cover consecutive strikes

- Suggested test name: `scoresConsecutiveStrikesWithCorrectLookahead`
- Rolls: `"X X 34 -- -- -- -- -- -- --"`.
- Expected total: `47`.
- Check: the first strike scores `10 + 10 + 3 = 23`; the second scores
  `10 + 3 + 4 = 17`; the `[3, 4]` frame scores `7`. Total:
  `23 + 17 + 7 = 47`.
- Purpose: catches incorrect strike lookahead when the next roll is also a
  strike.

#### 11. Score a spare in the tenth frame

- Suggested test name: `countsTheTenthFrameSpareBonus`
- Rolls: `"-- -- -- -- -- -- -- -- -- 5/ 3"`.
- Expected total: `13`.
- Check: the first nine frames score zero; the tenth-frame spare scores
  `10 + 3 = 13`.
- Purpose: verifies the tenth frame gets one bonus roll and that it belongs
  to the tenth frame, not an additional frame.

#### 12. Score a strike in the tenth frame

- Suggested test name: `countsBothTenthFrameStrikeBonusRolls`
- Rolls: `"-- -- -- -- -- -- -- -- -- X 34"`.
- Expected total: `17`.
- Check: the first nine frames score zero; the tenth-frame strike scores
  `10 + 3 + 4 = 17`.
- Purpose: verifies the tenth frame gets two bonus rolls.

#### 13. Cover the README's all-spare example

- Suggested test name: `scoresTenSpareFramesAs150`
- Rolls: `"5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5"`.
- Expected total: `150`.
- Check: the first nine spares score `15` each; the tenth-frame spare scores
  `15`. Total: `10 × 15 = 150`.
- Purpose: confirms repeated spare lookahead and the README example.

#### 14. Cover the perfect game

- Suggested test name: `scoresTwelveStrikesAs300`
- Rolls: `"X X X X X X X X X X X X"`.
- Expected total: `300`.
- Check: each of ten frames scores `30`, including the tenth frame's two
  bonus rolls. Total: `10 × 30 = 300`.
- Purpose: confirms consecutive strikes throughout the game and the
  tenth-frame strike bonus; this is the README's all-strike example.

## Unit-test conventions

- Keep each test focused on one behavior. Scoring tests assert only the final
  game total; count-validation tests assert the documented exception.
- Use the project's existing JUnit 5 setup and ordinary unit tests (no
  `@SpringBootTest` for isolated scorer tests).
- Keep notation explicit in each test so frame and bonus behavior can be
  reviewed without debugging test-data helpers.
- Separate frames with a single space in test roll strings. Keep each frame's
  rolls together: two rolls for an open frame (`34`), the first roll and `/`
  for a spare (`5/`). Separate tenth-frame bonus rolls from the frame notation
  with a space; keep the two strike bonus rolls together (`5/ 3` or `X 34`).
- Do not add pin-count or pin-value validation tests; these behaviors are
  outside the agreed scope.

## Completion checklist

- [x] All four roll-count validation cases pass before scoring implementation
  begins.
- [x] All fourteen cases are marked DONE in Progress tracking.
- [x] Test results were reported at each TDD checkpoint before progressing.
- [x] The complete Maven test suite passes (`.\mvnw.cmd test`: 35 tests, 0 failures/errors) after introducing Frame and Roll models.
- [x] The approved Frame-model refactoring preserves the existing scoring behavior and tests.

## Completed improvement: Model parsed frames

The bowling notation is parsed into an immutable list of `Frame` records
before scoring. This keeps the public `score(String rolls)` API and current
documented behavior unchanged.

### Implementation

- `Frame` stores immutable lists of its own `Roll` values and any bonus-roll
  values, and calculates its score from those values.
- `BowlingGameParser.parse` normalizes notation, validates
  the required rolls, and returns ten frames. For regular strikes and spares,
  bonus rolls are looked ahead without consuming rolls belonging to the next
  frame. The tenth frame consumes its own bonus rolls.
- `BowlingGameScorer` sums the parsed frame scores instead of maintaining
  scoring logic over indexes in the flattened roll string.

### Verification and follow-up

The full Maven suite passes with 35 tests. Review whether the explicit frame
model and `RollCursor` make the parser easier to maintain.

### Roll model assessment and prototype

A `Roll` type is not clearly beneficial for the current total-score-only
requirements: a notation-based type must still receive prior-roll context to
interpret `/`, while a resolved-value type moves that context into parsing.
Either approach risks relocating the existing symbol switch without reducing
complexity. A prototype was implemented and retained for review. `Roll`
represents one normalized notation character and resolves its pin count given
the preceding roll's pin count. `Frame` now stores lists of `Roll` values for
its rolls and bonuses, and the parser constructs those values. The public
scoring API and validation scope remain unchanged.

The type makes per-roll semantics explicit and moves notation-to-pin
conversion out of `Frame`, but the spare still requires context, notation
validation remains in the validator, and parser/model code now creates and
holds additional roll objects. Since `Frame` is the only consumer, this is a
modest clarity benefit rather than a clear reduction in overall complexity.
The prototype is retained as an intentional design choice for now; revisit it
if the extra representation becomes burdensome or if roll behavior gains
additional consumers.

Focused `Roll`, `Frame`, parser, and scorer tests pass, and the complete Maven
suite passes with 35 tests.
