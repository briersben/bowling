---
name: bowling-code-improvements
description: "Analyze the Bowling project for worthwhile Java code improvements, clean-code and design issues, duplication, readability, reuse, refactoring opportunities, and appropriate JDK 21/25 features. Use when asked to review, assess, prioritize, or implement Bowling codebase improvements."
argument-hint: "Optionally name an area to focus on, such as scorer, validation, API, tests, or Java modernization."
user-invocable: true
---

# Bowling Code Improvements

Analyze the Bowling project for concrete, worthwhile improvements. Start with
an evidence-based assessment; do not change code unless the user asks to
implement a candidate or approves one.

## Project context

- The repository root is the Bowling workspace containing this skill.
- Read [project instructions](../../copilot-instructions.md),
  [README](../../../README.md), and
  [implementation plan](../../../IMPLEMENTATION_PLAN.md) before assessing.
- The project uses Java 25, Maven, Spring Boot, and JUnit 5.
- The README and project instructions define the intended bowling behavior and
  current scope. In particular, pin-count and pin-value validation are out of
  scope unless the user requests them.
- The implementation plan tracks the original kata steps. Check its status,
  but do not treat completed steps as proof that no other improvements exist
  or repeat work already covered there.

## Assessment procedure

1. Check `git status` and inspect relevant diffs before reading for changes.
   Preserve user work; do not overwrite or revert unrelated changes.
2. Inspect the current production code, its callers, relevant tests, and build
   configuration. Follow behavior across boundaries when needed (notation
   parsing and validation, scoring, Spring wiring, and HTTP handling); don't
   judge a method in isolation.
3. Look for demonstrable opportunities in correctness and edge cases,
   readability, method complexity, cohesion, coupling, duplication, reuse,
   test quality, and maintainability. Notice methods with excessive
   responsibilities, branching, nesting, or hard-to-follow control flow;
   recommend decomposition only when it makes behavior easier to understand
   or change. Consider Java 21/25 language or library features only where they
   make the existing code simpler or safer without obscuring the bowling
   rules or unnecessarily raising compatibility requirements.
4. Check candidates against the README, project instructions, tests, and
   implementation plan. Do not present intentional scope boundaries or
   subjective preferences as defects. Avoid speculative redesigns and
   unrelated style changes.
5. Report only actionable candidates supported by code evidence. For each,
   include:
   - a concise title and the specific evidence (file and line or symbol);
   - the improvement and its practical benefit;
   - affected behavior and likely regression risks;
   - a focused test or verification approach;
   - a relative priority (impact, effort, and risk).
6. Rank the candidates and identify the best next small improvement. Then ask
   the user how they want to proceed: keep the assessment only, select a
   specific candidate to implement, or implement the top recommendation.
   Do not edit files, create backlog entries, or implement a refactor during
   assessment-only requests. If no worthwhile change is supported, say so
   and ask whether the user wants to explore another area.

Keep the assessment concise and distinguish confirmed defects from optional
refactoring ideas. Prefer a small number of well-supported findings over a
long list of generic clean-code suggestions.

## Implementing a selected improvement

Only proceed when the user explicitly asks to implement a candidate or
approves one. Implement one bounded improvement at a time; do not silently
expand scope to neighboring cleanup.

Follow the project's test-first workflow:

1. Add or update only the focused test for the selected behavior. Run the
   narrowest relevant test from the repository root, typically:
   `.\mvnw.cmd "-Dtest=<TestClass>#<testMethod>" test`
2. Report the test and its result. If it fails for the expected behavior,
   stop and wait for the user's explicit approval before changing production
   code. If it passes unexpectedly or fails for setup/environment reasons,
   explain that and ask how to proceed; do not manufacture a red test.
3. After approval, make the smallest production change that satisfies the
   test, rerun the focused test, report the green result, and stop for
   approval before any refactoring or additional changes.
4. Refactor only with explicit approval. Rerun relevant tests after
   refactoring; run the full Maven suite when the change affects broader
   behavior or at the user's request.
5. Update documentation only when the selected change alters documented
   behavior or usage. Do not commit or push unless explicitly requested.
