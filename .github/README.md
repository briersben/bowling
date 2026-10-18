# GitHub Copilot configuration

Use this directory for repository-specific GitHub Copilot customization:

- `copilot-instructions.md` contains instructions that apply across the repository.
- `instructions/` contains path-specific instruction files named `*.instructions.md`.
- `prompts/` contains reusable prompt files named `*.prompt.md`.
- `agents/` contains custom agent profiles named `*.agent.md`.
- `skills/` contains skill directories, each with a `SKILL.md` file.

Keep shared project requirements in the root `copilot-instructions.md`; use the other locations for focused, reusable guidance.
