Design Evaluation Summary (Legends of Valor)

Overview

Our implementation evolved from a loosely coupled, event-driven prototype into a more explicit state-oriented architecture with a centralized board authority. The primary design goal was to preserve clarity of game flow while enforcing correct Legends of Valor rules (lanes, movement constraints, combat triggers, and nexus conditions).

This document summarizes the changes made from the initial implementation to the current design, focusing on the reasoning behind those decisions and a post-fact evaluation of their effectiveness.

⸻

Major Design Changes and Rationale

1. Centralizing Spatial Authority in LoVBoard

Initial approach:
Hero and monster positions were partially inferred or duplicated across systems (movement logic, combat checks, rendering). Combat range and adjacency relied on generic Position logic without board awareness.

Final approach:
LoVBoard became the single source of truth for:
•	Hero and monster positions
•	Lane membership
•	Tile accessibility
•	Adjacency detection
•	Nexus detection
•	Battle triggering

All spatial queries (e.g., “can these two units fight?”) are now resolved through the board rather than through free-floating geometry checks.

Why this was chosen:
Legends of Valor has strict lane rules and asymmetric constraints (walls, obstacles, nexus rows). These rules cannot be safely enforced without full board context.

Evaluation:
This decision significantly reduced bugs around invalid attacks, phantom adjacency, and incorrect battle triggers. It also made debugging easier by localizing spatial logic.

⸻

2. Guaranteed Lane Traversability via Spine Columns

Initial approach:
Board generation allowed obstacles to randomly block both columns in a lane, resulting in unwinnable or soft-locked games.

Final approach:
Each lane now has a guaranteed open spine column, while at most one obstacle is allowed per lane. Hard walls remain fixed.

Why this was chosen:
Legends of Valor guarantees forward progress. Random generation must respect playability constraints.

Evaluation:
This fully resolved traversal deadlocks without removing randomness. The board remains varied but always solvable.

⸻

3. Explicit Exploration → Battle State Transition

Initial approach:
Battle triggers were implicit and sometimes missed due to movement order or ambiguous adjacency checks.

Final approach:
LoVBoard explicitly emits WorldEvent.BATTLE_TRIGGERED when a hero and monster become adjacent in the same lane. The ExplorationState responds by extracting only the relevant heroes and monsters and transitioning cleanly into BattleState.

Why this was chosen:
State transitions should be deterministic and event-driven, not inferred indirectly.

Evaluation:
Battle triggering is now consistent, predictable, and lane-scoped. Multi-hero battles work correctly.

⸻

4. Single-Turn Per Hero Exploration Loop

Initial approach:
Exploration input handling was ambiguous, sometimes allowing repeated moves or skipped turns.

Final approach:
Exploration now enforces:
•	Exactly one action per hero per round
•	Monster movement only after all heroes act
•	Turn index tracked explicitly

Why this was chosen:
Legends of Valor rules require synchronized turns across heroes before monsters act.

Evaluation:
Turn order bugs were eliminated. The flow matches the spec closely and is easy to reason about.

⸻

5. Nexus Detection and Game Termination

Initial approach:
Victory conditions were scattered or inferred from row positions without tile ownership checks.

Final approach:
LoVBoard exposes:
•	isHeroOnMonsterNexus
•	anyHeroOnMonsterNexus
•	anyMonsterOnHeroNexus

Game termination is triggered via explicit WorldEvent.HERO_WIN or WorldEvent.MONSTER_WIN.

Why this was chosen:
Nexus tiles have semantic meaning beyond coordinates. Ownership must be checked explicitly.

Evaluation:
Victory conditions are now unambiguous and future-proof (e.g., market transitions, scoring).

⸻

6. Cleaner Separation of Responsibilities

Initial approach:
State classes sometimes handled logic beyond their scope (e.g., deciding global termination).

Final approach:
•	LoVBoard: rules and world logic
•	ExplorationState: input handling and turn sequencing
•	BattleState: combat flow only
•	GameContext: global lifecycle and outcomes

Why this was chosen:
This separation follows the Single Responsibility Principle and reduces cascading changes.

Evaluation:
The architecture became easier to extend and reason about. Adding new states (Market, GameOver) required minimal refactoring.

⸻

7. Combat System Refactoring and Clarification

Initial approach:
Combat logic relied heavily on shared Position data and generic range checks. Attacks could silently fail when position data was stale or when adjacency rules were unclear. Combat resolution and victory detection were partially implicit and spread across systems.

Final approach:
Combat was refactored into a clearer, round-based model centered around ValorBattle, with:
•	Explicit round resolution (resolveRound)
•	Clear separation between hero actions, monster AI turns, and end-of-round regeneration
•	Board-aware adjacency and range validation via updated combat rules
•	Explicit combat outcomes (ONGOING, HERO_VICTORY, MONSTER_VICTORY)

Hero and monster positions are now synchronized with the board, ensuring combat checks reflect the live game state.

Why this was chosen:
Combat is a critical gameplay loop and required deterministic behavior, clear feedback, and consistent rule enforcement. Centralizing combat flow while delegating spatial validation to the board eliminated ambiguity.

Evaluation:
Combat is now predictable, debuggable, and correctly scoped. Invalid attacks are prevented early, combat resolution is explicit, and victory conditions are detected cleanly. The system is also extensible for future mechanics such as rewards, status effects, or multi-round encounters.

⸻

Post-Fact Evaluation

Overall, the final design met and exceeded initial expectations:
•	All core Legends of Valor rules are enforced correctly
•	Random board generation remains fair and playable
•	State transitions are explicit and debuggable
•	Combat and exploration no longer leak into each other

The largest improvement came from treating the board as an authority, rather than a passive grid. This shift simplified nearly every downstream system, including combat validation and state transitions.

If revisiting this design, the same architectural decisions would be made again.

Design Patterns Used

8. State Pattern for Game Flow Control

Where used:
•	ExplorationState
•	BattleState
•	MarketState
•	GameOverState
•	StateManager

Description:
The game uses an explicit State Pattern to model high-level gameplay phases. Each state encapsulates its own behavior for input handling, updates, and transitions.

Why this was chosen:
Legends of Valor has mutually exclusive phases with very different rules. Mixing exploration logic, combat resolution, and market interaction in a single loop would have led to tangled conditionals and fragile flow control.

Evaluation:
This pattern significantly improved readability and correctness. State transitions are explicit and easy to trace, and adding new phases required minimal changes to existing code.

⸻

9. Command Pattern for Player and AI Actions

Where used:
•	ExplorationAction and subclasses (MoveAction, TeleportAction, RecallAction, QuitGameAction)
•	HeroAction and subclasses (AttackAction, SpellcastAction, PotionAction, EquipAction, SkipAction)

Description:
All player and AI actions are represented as command objects that encapsulate both intent and required parameters. Actions are created by an action manager and executed by the appropriate system.

Why this was chosen:
Commands decouple input handling from execution logic. This allows the same execution pipeline to be reused for player-driven and AI-driven behavior.

Evaluation:
This made action handling extensible and testable. New actions were added without modifying core loops, and invalid actions could be safely ignored without destabilizing the game.

⸻

10. Single Source of Truth (Board Authority Pattern)

Where used:
•	LoVBoard as the authoritative owner of:
•	positions
•	lane logic
•	tile accessibility
•	adjacency
•	nexus state

Description:
The board functions as a central authority for all spatial and rule-based decisions.

Why this was chosen:
Distributed rule checks led to inconsistent behavior and hard-to-debug errors. Centralizing this logic ensured correctness and reduced duplication.

Evaluation:
This pattern was the most impactful design decision. It eliminated entire classes of bugs related to movement, combat eligibility, and victory detection.

⸻

11. Strategy Pattern for Monster AI Behavior

Where used:
•	MonsterAI
•	decideMove(...)
•	tryAttack(...)

Description:
Monster behavior is encapsulated behind an AI strategy that can decide movement and combat actions based on current board conditions.

Why this was chosen:
Separating AI logic from board and state logic prevents rule contamination and allows AI behavior to evolve independently.

Evaluation:
This enabled clean monster turns and made AI logic easier to reason about. Future AI variations can be introduced without modifying the board or state machine.

⸻

12. Factory Pattern for Board and Monster Generation

Where used:
•	MonsterFactory
•	LoVTileFactory

Description:
Factories are used to create monsters and tiles without exposing construction details to higher-level systems.

Why this was chosen:
Game entities require non-trivial initialization logic that depends on difficulty, level, or randomness.

Evaluation:
Factories simplified initialization code and centralized balancing logic. Changes to monster scaling or tile probabilities did not ripple through the codebase.

⸻

13. Template Method Pattern in Combat Resolution

Where used:
•	ValorBattle.resolveRound()

Description:
The combat round follows a fixed structure:
1.	Print battle state
2.	Heroes act
3.	Monsters act
4.	End-of-round regeneration
5.	Victory check

While individual actions vary, the round structure is invariant.

Why this was chosen:
Combat needed a predictable structure while still allowing flexible action behavior.

Evaluation:
This ensured combat rounds were consistent and easy to debug. The flow is clear, and adding new action types does not affect round sequencing.

⸻

Final Evaluation of Pattern Usage

The combination of State, Command, Strategy, Factory, and a central authority model resulted in a system that is:
•	Modular
•	Rule-correct
•	Extensible
•	Easy to reason about

Patterns were applied where they reduced complexity rather than for abstraction’s sake. In retrospect, these choices aligned well with the problem domain and significantly reduced integration bugs.
