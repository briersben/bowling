---
name: bowling-tdd-next-step
description: "Continue the next pending Bowling kata implementation step using strict TDD. Use when asked to continue the bowling plan, implement the next step, or add the next bowling test. Add and run one test, then stop for user approval before production changes."
argument-hint: "Optionally name a specific plan step; otherwise continue the earliest pending step."
user-invocable: true
---

# Continue the Bowling TDD Plan

Use this skill to advance one step at a time from
[IMPLEMENTATION_PLAN.md](../../../IMPLEMENTATION_PLAN.md).

## Non-negotiable TDD pause

The user must inspect the test result before production code is changed.

1. Add only the test for the current next step.
2. Run only that test.
3. Report the test, expected behavior, and actual result.
4. Stop and ask the user whether to continue. Do not edit production code,
   add another test, refactor, or run a broad suite before explicit approval.

If the focused test fails at the expected assertion, mark its plan status
`RED — awaiting review` and wait for approval. If it passes unexpectedly,
report that it did not produce a red result and ask whether to adjust the test;
do not change production. If it fails to compile or fails for unrelated setup,
report the issue as a blocker, not a meaningful red result, and wait for
direction before making setup changes.

After the user approves a meaningful red test, make the smallest production
change that satisfies only that test. Rerun the focused test, report the green
result, mark that step `DONE`, and stop again before starting another test.

## Startup and step selection

1. Read the repository's `.github/copilot-instructions.md` and
   `IMPLEMENTATION_PLAN.md`.
2. Check `git status` before editing. Preserve existing user changes; do not
   revert or overwrite unrelated work.
3. Read the current test and production files relevant to the next step.
4. If the user names a step, use that step only if it matches the plan and is
   not already done. Otherwise select the earliest `PENDING` step.
5. If the earliest step is marked `RED — awaiting review`, do not replace it
   or continue; ask whether the user approves implementing the current red
   test.
6. If every planned step is `DONE`, report that the plan is complete and ask
   what the user wants next. Do not invent additional tests or scope.

## Test-only stage

1. Add just one focused test to
   `src/test/java/com/bnpp/katas/bowling/BowlingGameScorerTest.java`, following
   existing style and the exact test name/input/expected result in the plan.
2. If the current plan step needs a different API or input shape, add only the
   test first. If that does not compile, stop and ask before adding any
   production scaffold.
3. Run the focused test from the repository root:
   `.\mvnw.cmd "-Dtest=BowlingGameScorerTest#<testMethod>" test`
4. Update only that step's progress status in the plan:
   - expected failing assertion: `RED — awaiting review`
   - unexpected pass: leave `PENDING` and explain the result
   - unrelated compilation/environment failure: `BLOCKED` with a brief reason
5. Stop. Use the ask-user interaction to request explicit approval to proceed
   after showing the result. Never infer approval from a previous step or
   from the original request to continue the plan.

## Production stage (only after approval)

1. Implement the minimum behavior required by the approved test.
2. Run only the focused test using the command above.
3. If it passes, mark the plan step `DONE`, report the change and result, and
   stop for approval before starting another planned test.
4. If it still fails, explain the result and continue only within the approved
   behavior; do not add further tests or broaden scope.

## Completion

When all planned steps are done, run the full suite with
`.\mvnw.cmd clean test`, update the plan's completion checklist, and report
the result. Do not commit or push unless the user explicitly requests it.
