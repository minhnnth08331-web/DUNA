# Sales Skills

> **This repo has moved.** Sales skills now live in the unified skills repo alongside CEO, YouTube, and general skills.
>
> **New home:** [github.com/TheCraigHewitt/skills](https://github.com/TheCraigHewitt/skills)
>
> ```bash
> npx skills@latest add TheCraigHewitt/skills/sales
> ```

---

**Your AI agent already writes code. Now teach it to sell.**

Most AI-generated sales content is generic, forgettable, and immediately deleted. These 21 skills give your AI agent the same frameworks, instincts, and pushback that experienced sales leaders use -- so the output actually works.

[![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE) [![Skills](https://img.shields.io/badge/Skills-21-green.svg)](#skills-21) [![Works with Claude Code](https://img.shields.io/badge/Works%20with-Claude%20Code-orange.svg)](https://claude.ai/code) [![Agent Skills Spec](https://img.shields.io/badge/Spec-Agent%20Skills-purple.svg)](https://agentskills.io)

Works with [Claude Code](https://claude.ai/code), [Cursor](https://cursor.com), [Windsurf](https://windsurf.ai), [Codex](https://openai.com/codex), and any tool that supports the [Agent Skills spec](https://agentskills.io).

---

## Install

### Claude Code (Plugin)

```bash
claude plugin add thecraighewitt/sales-skills
```

### Claude Code (Manual)

```bash
npx skills add thecraighewitt/sales-skills
```

### Cursor / Windsurf / Other Agents

Clone into your project's `.agents/skills/` directory:

```bash
git clone https://github.com/thecraighewitt/sales-skills.git .agents/skills/sales-skills
```

Or add as a submodule:

```bash
git submodule add https://github.com/thecraighewitt/sales-skills.git .agents/skills/sales-skills
```

---

## Quick Start: Your First Skill in 60 Seconds

After installing, open your AI agent and type:

```
Write me a cold email to a VP of Sales at a mid-market SaaS company.
We sell AI-powered call coaching that increases close rates by 23%.
```

The agent loads the `cold-email` skill, asks you targeted follow-ups about your ICP and proof points, then outputs:

```
Subject: quick question about [Company]'s ramp time

Hey Sarah,

Noticed you're hiring 4 AEs right now. Most sales leaders I talk to
at that stage say ramp time is killing their numbers.

We helped [Similar Company] cut AE ramp from 6 months to 11 weeks
using AI call coaching that flags exactly where reps lose deals.

Worth a 15-minute look?
```

Plain text. Under 60 words. One ask. No HTML templates, no "I hope this email finds you well."

For even better output, run `sales-context` first to set up your ICP, value prop, and proof points. Every skill reads that file automatically.

---

## How It Works

1. **Start with `sales-context`.** Run it first to create your `.agents/sales-context.md` file -- your ICP, value prop, sales motion, and proof points in one place. Every other skill reads this automatically.
2. **Use skills naturally.** Ask your AI agent to write a cold email, prep for a discovery call, or review your pipeline. The right skill activates based on your request.
3. **Skills cross-reference each other.** `outbound-sequence` orchestrates your channel skills. `win-loss-analysis` feeds back into `competitive-intel`. The 21 skills work as a system, not a collection of isolated prompts.

---

## Why Agent Skills?

One-off prompts forget everything. You paste in context, get output, lose the context, start over.

Agent skills are persistent, cross-referencing, and context-aware. Your sales context file is written once and read by every skill. Your buyer personas inform your discovery calls, which inform your demos, which inform your proposals. When you run a win-loss analysis, the insights feed back into your competitive intel and buyer personas.

This is a sales system with memory -- not 21 separate prompts taped together.

---

## Who Is This For?

- **Startup founders who sell.** You don't have a sales team yet. You need frameworks that work without a 50-person enablement org behind them.
- **Sales teams scaling outbound.** You're building sequences, writing cold emails, prepping for demos -- and your AI agent should be better at all of it.
- **Revenue ops leaders.** You're designing comp plans, running pipeline reviews, building forecasts. These skills encode the frameworks you'd teach a new hire.

---

## Skills (21)

### Foundation

| Skill | What It Does |
|-------|-------------|
| [sales-context](skills/sales-context/) | Build your sales playbook -- ICP, value prop, sales motion, proof points. Every other skill reads this first. |
| [buyer-persona](skills/buyer-persona/) | Deep buyer personas -- pain points, decision criteria, day-in-the-life, champion vs. blocker profiles. |
| [competitive-intel](skills/competitive-intel/) | Battle cards, positioning against competitors, trap-setting questions, win/loss patterns. |

### Prospecting & Outbound

| Skill | What It Does |
|-------|-------------|
| [cold-email](skills/cold-email/) | Cold emails that get replies. Subject lines, personalization, follow-up sequences. Plain text, under 100 words, one ask. |
| [cold-call](skills/cold-call/) | Cold call scripts -- openers that don't get hung up on, gatekeeper navigation, voicemail that earns callbacks. |
| [linkedin-outreach](skills/linkedin-outreach/) | Connection requests, InMails, DM sequences that don't read like spam. |
| [direct-mail](skills/direct-mail/) | Physical mail that lands on desks, not in trash -- dimensional mailers, handwritten notes, gift sequences. |
| [lead-research](skills/lead-research/) | Account and contact research -- trigger events, org charts, tech stack, intent signals. |
| [outbound-sequence](skills/outbound-sequence/) | Multi-channel sequence design -- cadence, timing, channel mix. Orchestrates the channel skills above. |
| [referral-intro](skills/referral-intro/) | Warm intro requests that make it easy for your connector to say yes. |
| [event-networking](skills/event-networking/) | Conference outreach -- pre-event targeting, booth conversations, post-event follow-up that converts. |

### Deal Execution

| Skill | What It Does |
|-------|-------------|
| [discovery-call](skills/discovery-call/) | Discovery call planning -- SPIN, MEDDIC, Gap Selling frameworks. The questions that surface real pain. |
| [demo-script](skills/demo-script/) | Demo scripts by persona and deal stage -- story arcs that connect features to outcomes, not feature tours. |
| [objection-handling](skills/objection-handling/) | Objection playbooks -- price, timing, competition, status quo. Responses that move deals forward, not just deflect. |

### Deal Close

| Skill | What It Does |
|-------|-------------|
| [proposal-pricing](skills/proposal-pricing/) | Proposals, SOWs, pricing presentations -- packaging, anchoring, ROI justification that makes the business case. |
| [negotiation](skills/negotiation/) | Negotiation prep -- BATNA analysis, concession planning, deal structure, knowing your red lines before you sit down. |

### Deal Intelligence

| Skill | What It Does |
|-------|-------------|
| [call-debrief](skills/call-debrief/) | Post-call analysis -- what actually happened, next steps, risk signals, coaching notes. |
| [win-loss-analysis](skills/win-loss-analysis/) | Pattern recognition across closed deals -- competitive insights, process gaps, what to fix next quarter. |

### Sales Ops

| Skill | What It Does |
|-------|-------------|
| [pipeline-review](skills/pipeline-review/) | Pipeline reviews -- deal scoring, risk flagging, the hard questions reps don't want to answer. |
| [forecast](skills/forecast/) | Sales forecasts -- weighted pipeline, conversion math, commit vs. upside vs. best-case. |
| [sales-comp](skills/sales-comp/) | Comp plan design -- OTE, quotas, accelerators, SPIFs. Model plans before you roll them out. |

---

## Skill Architecture

```
                         ┌─────────────────────────────────┐
                         │           FOUNDATION             │
                         │   sales-context (read by all)    │
                         │   buyer-persona ↔ competitive-   │
                         │                   intel          │
                         └───────────────┬─────────────────┘
                                         │
    ┌────────────────────────────────────┼──────────────────────────────┐
    ▼                                    ▼                              ▼
┌──────────────────┐  ┌──────────────────────────┐  ┌─────────────────────┐
│   PROSPECTING    │  │     DEAL EXECUTION       │  │     SALES OPS       │
├──────────────────┤  ├──────────────────────────┤  ├─────────────────────┤
│ lead-research ───┐  │ discovery-call           │  │ pipeline-review     │
│ cold-email      ││  │ demo-script              │  │ forecast            │
│ cold-call       ││  │ objection-handling       │  │ sales-comp          │
│ linkedin-outreach┘  ├──────────────────────────┤  └─────────────────────┘
│ direct-mail      │  │     DEAL CLOSE           │
│ referral-intro   │  ├──────────────────────────┤
│ event-networking │  │ proposal-pricing         │
│                  │  │ negotiation              │
│ outbound-sequence│  ├──────────────────────────┤
│ (orchestrates ↑) │  │   DEAL INTELLIGENCE      │
└──────────────────┘  ├──────────────────────────┤
                      │ call-debrief             │
                      │ win-loss-analysis ──→    │
                      │   feeds competitive-intel│
                      └──────────────────────────┘
```

---

## Roadmap

Coming soon:

- **CRM integrations** -- Salesforce and HubSpot skills for deal data sync
- **Email warmup skill** -- Domain warmup playbook and deliverability diagnostics
- **Sales hiring skill** -- Interview scorecards, rep profile matching, onboarding plans
- **Enablement skill** -- Training content, battlecard distribution, ramp programs

Want to suggest a skill? [Open an issue](https://github.com/thecraighewitt/sales-skills/issues).

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on improving existing skills or adding new ones.

---

## About

Built by **[Craig Hewitt](https://agentprime.io)** -- founder/CEO of [Castos](https://castos.com) ($1.5M ARR podcast hosting), building [FounderLevel](https://founderlevel.co) on YouTube, and running [AgentPrime](https://agentprime.io) (AI-powered revenue operations for $5M+ companies).

These skills aren't from a sales textbook. They're from 8+ years of building and running real sales processes across SaaS, consulting, and agency businesses -- writing the cold emails, running the discovery calls, losing the deals, figuring out why, and building systems so the next one closes.

If you're a founder, sales leader, or revenue ops person trying to figure out how AI fits into your sales motion, this is the toolkit I wish I'd had.

- [AgentPrime](https://agentprime.io) -- AI-powered revenue operations consulting
- [YouTube](https://youtube.com/@founderlevel) -- AI + founder leadership (3-4 videos/week)
- [Substack](https://founderlevel.substack.com) -- Weekly newsletter
- [X/Twitter](https://twitter.com/thecraighewitt)

---

## License

MIT -- see [LICENSE](LICENSE).
