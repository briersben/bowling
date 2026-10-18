# Copilot instructions

## Project requirements

The [README](../README.md) is the full source of truth for the Bowling Kata. Implement a scorer that returns the total for one valid American ten-pin bowling game:

- A game has ten frames. A frame normally has up to two rolls; a strike (all ten pins on the first roll) ends the frame.
- An open frame scores the pins knocked down in its rolls. A spare (all ten pins in two rolls) scores ten plus the next roll. A strike scores ten plus the next two rolls.
- A spare in the tenth frame earns one bonus roll; a strike earns two. Bonus rolls count only toward the tenth-frame score: they are not extra frames and do not start another bonus sequence.
- Return the game total only; intermediate frame scores are out of scope.
- Assume rolls and game length are valid. Checking roll validity or the correct number of rolls/frames is explicitly out of scope unless requested.
- Cover the README's example cases: twelve strikes score 300; ten 9-and-miss frames score 90; ten 5-spare frames with a final bonus 5 score 150.

## Development guidance

- Use test-driven development: add or update a focused test before production changes, then run the relevant tests with `.\mvnw.cmd test` from the repository root (or `./mvnw test` on Unix-like systems). Prefer clear, high-quality code and keep changes within the requested scope.
- This is a Java 25 Maven project. Put application code under `src/main/java/com/bnpp/katas/bowling` and tests under the matching package in `src/test/java`.
- Follow the [Udacity Git Style Guide](https://udacity.github.io/git-styleguide/) for Git usage and commit messages.
