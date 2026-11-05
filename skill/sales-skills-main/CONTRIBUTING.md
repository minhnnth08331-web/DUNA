# Contributing to Sales Skills

Thanks for your interest in improving these sales skills. Contributions are welcome — whether it's fixing a typo, improving a framework, or adding a new skill.

## How to Contribute

### Improving an Existing Skill

1. Fork the repo
2. Create a branch: `fix/skill-name-description`
3. Make your changes
4. Run `./validate-skills.sh` to verify
5. Open a PR with a clear description of what you changed and why

### Adding a New Skill

1. Fork the repo
2. Create a branch: `feature/skill-name`
3. Create `skills/skill-name/SKILL.md` following the template below
4. Add your skill to `.claude-plugin/marketplace.json`
5. Add your skill to `VERSIONS.md`
6. Run `./validate-skills.sh`
7. Open a PR

## Skill Template

Every `SKILL.md` must follow this structure:

```yaml
---
name: skill-name
description: "When the user wants to [task]. Also use when the user says '[trigger 1],' '[trigger 2],' '[trigger 3].' For [related task], see [other-skill]."
metadata:
  version: 1.0.0
---
```

### Body Structure

1. **Expert persona** — "You are a [domain] expert..."
2. **Before Starting** — Check for `.agents/sales-context.md`
3. **Context questions** — What to ask if context is missing
4. **Core principles** — 3-5 opinionated, practitioner-sourced principles
5. **Frameworks/process** — Step-by-step methodology
6. **Examples/templates** — Concrete output examples
7. **Related skills** — Cross-references

### Rules

- Keep skills under 500 lines
- Name must match directory name exactly
- Name: lowercase letters, numbers, hyphens only
- Description must include trigger phrases
- Cross-reference related skills in both the description and body
- Write from practitioner experience, not textbook theory
- Be opinionated — "do this, not that" is more useful than "consider these options"

## Style Guidelines

- **Direct and practical.** Busy sales teams need clear instructions, not essays.
- **Frameworks over theory.** Give people a structure they can follow.
- **Examples over explanations.** Show, don't tell.
- **Opinionated.** Take a stance. "Use 3-line cold emails" is better than "email length varies."
- **Channel-specific.** Don't mix LinkedIn advice into the cold email skill.

## Commit Messages

Use conventional commits:

- `feat(skill-name): add new skill`
- `fix(skill-name): correct discovery question framework`
- `docs: update README with new skill`
- `chore: update validation script`

## Questions?

Open an issue or reach out to [@thecraighewitt](https://twitter.com/thecraighewitt).
