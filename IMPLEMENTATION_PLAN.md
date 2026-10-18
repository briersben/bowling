# Bowling Scorer Implementation Plan

## Goal and scope

Implement a scorer for American ten-pin bowling that returns the total score
only. Validate that the roll sequence can represent exactly ten complete
frames, including any bonus rolls required in the tenth frame. Reject a roll
sequence with too few or extra rolls using `IllegalArgumentException`.
Validating pin counts, roll values, and intermediate frame scores remains out
of scope.

The project now has a `BowlingGameScorer` API stub and one failing
roll-count-validation test, `rejectsTooFewRollsToCompleteTenFrames`.
Prioritize the remaining roll-count validation tests below before adding
scoring tests.

The scorer belongs in `com.bnpp.katas.bowling` and should be a Spring-managed
service (`@Service`). Inject it through constructors into any Spring-managed
consumers. Keep focused scorer tests as ordinary JUnit tests without starting
Spring; use Mockito only if a test needs to isolate a real collaborator. Put
tests in `src/test/java/com/bnpp/katas/bowling/BowlingGameScorerTest.java`.

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

The API scaffold and first roll-count test have already been created. Continue
with that test's red-green cycle, then add only the next planned test. Do not
implement scoring as part of setting up a test. If a later focused test fails
to compile for an API/setup reason, report that separately from a meaningful
failing assertion and stop for review.

If any test fails due to an unrelated build, environment, or test-discovery
problem, report that separately and resolve it with the user; do not call it a
meaningful red step.

## Test sequence

A strike ends its frame after one roll. `0` means a gutter roll. When a
sequence says “repeat,” repeat the listed rolls exactly the stated number of
times. Each numbered case is a separate Red-Green cycle. The invalid sequences
in the first phase intentionally test the new roll-count validation
requirement.

### Phase 1: Validate that the sequence completes exactly ten frames

Run these count-validation cycles before adding scoring tests. After each
test, stop at red for review, then make the smallest approved change and stop
again at green.

#### 1. Reject too few rolls to complete ten frames

- Suggested test name: `rejectsTooFewRollsToCompleteTenFrames`
- Rolls: `[0]` repeated 19 times.
- Expected result: throw `IllegalArgumentException`.
- Check: nineteen gutter rolls form only nine complete two-roll frames and
  leave the tenth frame incomplete.
- Purpose: establish incomplete regular-frame detection before scoring cases.

#### 2. Reject a tenth-frame spare without its bonus roll

- Suggested test name: `rejectsTenthFrameSpareWithoutBonusRoll`
- Rolls: `[0, 0]` repeated 9 times, followed by `[5, 5]` (20 rolls).
- Expected result: throw `IllegalArgumentException`.
- Check: the tenth-frame spare requires exactly one bonus roll.
- Purpose: verify that a spare's required tenth-frame bonus is part of a
  complete game.

#### 3. Reject a tenth-frame strike without both bonus rolls

- Suggested test name: `rejectsTenthFrameStrikeWithoutBothBonusRolls`
- Rolls: `[0, 0]` repeated 9 times, followed by `[10, 3]` (20 rolls).
- Expected result: throw `IllegalArgumentException`.
- Check: the tenth-frame strike requires two bonus rolls; only one is present.
- Purpose: verify that the tenth-frame strike cannot finish with only one
  bonus roll.

#### 4. Reject extra rolls after ten frames

- Suggested test name: `rejectsExtraRollsAfterTenFrames`
- Rolls: `[1, 2]` repeated 10 times, followed by `[1]` (21 rolls).
- Expected result: throw `IllegalArgumentException`.
- Check: the ten open frames consume exactly 20 rolls; the twenty-first roll
  cannot be assigned to a frame or a tenth-frame bonus.
- Purpose: ensure the scorer does not silently ignore extra rolls.

### Phase 2: Score complete valid games

All sequences in this phase are complete, valid games.

#### 5. Sum a basic open game

- Suggested test name: `scoresTenOpenFrames`
- Rolls: `[1, 2]` repeated 10 times.
- Expected total: `30`.
- Check: ten frames score `1 + 2 = 3` each; `10 × 3 = 30`.
- Purpose: establishes the total-score API and ordinary two-roll frame
  accumulation. Add it only after the roll-count validation phase is
  complete.

#### 6. Score a gutter game

- Suggested test name: `scoresGutterGameAsZero`
- Rolls: `[0, 0]` repeated 10 times.
- Expected total: `0`.
- Check: all twenty rolls knock down zero pins.
- Purpose: verifies the zero-score boundary after basic accumulation works.

#### 7. Cover the README's open-frame example

- Suggested test name: `scoresTenNineAndMissFrames`
- Rolls: `[9, 0]` repeated 10 times.
- Expected total: `90`.
- Check: ten open frames score `9` each; `10 × 9 = 90`.
- Purpose: confirms the kata's all-open example.

#### 8. Add a spare bonus

- Suggested test name: `addsTheNextRollToASpare`
- Rolls: `[5, 5, 3, 0]`, followed by `[0, 0]` repeated 8 times.
- Expected total: `16`.
- Check: the spare frame scores `10 + 3 = 13`; the following frame scores
  `3`; the remaining frames score zero. Total: `13 + 3 = 16`.
- Purpose: verifies a spare uses exactly the next roll as its bonus.

#### 9. Add a strike bonus

- Suggested test name: `addsTheNextTwoRollsToAStrike`
- Rolls: `[10, 3, 4]`, followed by `[0, 0]` repeated 8 times.
- Expected total: `24`.
- Check: the strike frame scores `10 + 3 + 4 = 17`; the next frame scores
  `3 + 4 = 7`; the remaining frames score zero. Total: `17 + 7 = 24`.
- Purpose: verifies that a strike ends its frame after one roll and receives
  the next two rolls as a bonus.

#### 10. Cover consecutive strikes

- Suggested test name: `scoresConsecutiveStrikesWithCorrectLookahead`
- Rolls: `[10, 10, 3, 4]`, followed by `[0, 0]` repeated 7 times.
- Expected total: `47`.
- Check: the first strike scores `10 + 10 + 3 = 23`; the second scores
  `10 + 3 + 4 = 17`; the `[3, 4]` frame scores `7`. Total:
  `23 + 17 + 7 = 47`.
- Purpose: catches incorrect strike lookahead when the next roll is also a
  strike.

#### 11. Score a spare in the tenth frame

- Suggested test name: `countsTheTenthFrameSpareBonus`
- Rolls: `[0, 0]` repeated 9 times, followed by `[5, 5, 3]`.
- Expected total: `13`.
- Check: the first nine frames score zero; the tenth-frame spare scores
  `10 + 3 = 13`.
- Purpose: verifies the tenth frame gets one bonus roll and that it belongs
  to the tenth frame, not an additional frame.

#### 12. Score a strike in the tenth frame

- Suggested test name: `countsBothTenthFrameStrikeBonusRolls`
- Rolls: `[0, 0]` repeated 9 times, followed by `[10, 3, 4]`.
- Expected total: `17`.
- Check: the first nine frames score zero; the tenth-frame strike scores
  `10 + 3 + 4 = 17`.
- Purpose: verifies the tenth frame gets two bonus rolls.

#### 13. Cover the README's all-spare example

- Suggested test name: `scoresTenSpareFramesAs150`
- Rolls: `[5, 5]` repeated 9 times, followed by `[5, 5, 5]`.
- Expected total: `150`.
- Check: the first nine spares score `15` each; the tenth-frame spare scores
  `15`. Total: `10 × 15 = 150`.
- Purpose: confirms repeated spare lookahead and the README example.

#### 14. Cover the perfect game

- Suggested test name: `scoresTwelveStrikesAs300`
- Rolls: `[10]` repeated 12 times.
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
- Keep the input rolls explicit in each test so the frame and bonus behavior
  can be reviewed without debugging test-data helpers.
- Do not add pin-count or pin-value validation tests; these behaviors are
  outside the agreed scope.

## Completion checklist

- [ ] All four roll-count validation cases pass before scoring implementation
  begins.
- [ ] Each of the fourteen cases has been added and run individually in order.
- [ ] Every red result was shown to the user, who had a chance to inspect it
  before implementation continued.
- [ ] Every green result was reported before starting the next test.
- [ ] The complete Maven test suite passes.
- [ ] Any approved refactoring preserves the individual scoring tests.
