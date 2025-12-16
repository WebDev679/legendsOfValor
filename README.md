Legends of Valor

A turn-based, lane-based strategy game inspired by Legends of Valor, implemented in Java.
The game features structured exploration, deterministic combat, AI-driven monsters, and explicit state transitions.

⸻

Requirements
•	Java 8 (or compatible JDK)
•	Bash shell

⸻

Building the Project

Use the provided build script:

```bash
./build.sh
```

This compiles all source files and prepares the project for execution.

If the script is not executable, run:

```bash
chmod +x build.sh
```


⸻

Running the Game

After building, start the game with:

```bash
./run.sh
```

If needed:

```bash
chmod +x run.sh
```


⸻

Game Overview

Exploration Phase
* Heroes act one at a time per round
* Supported actions:
* Move (up/down within lane)
* Teleport
* Recall to nexus
* Quit game
* Monsters move after all heroes act
* Movement obeys lane rules, obstacles, and walls

Battle Phase
* Triggered automatically when a hero and monster become adjacent in the same lane
* Only heroes and monsters in the affected lane participate
* Each round consists of:
1.	Hero actions
2.	Monster AI actions
3.	End-of-round regeneration
* attle ends when one side is eliminated

Victory Conditions
* Hero Victory: Any hero reaches the monster nexus
* Monster Victory: Any monster reaches the hero nexus

Game termination and state transitions are handled explicitly through game states.

⸻

Controls

During exploration:
* W – Move up 
* S – Move down 
* T – Teleport 
* R – Recall 
* Q – Quit

During battle:
Follow on-screen prompts to select actions and targets

⸻

Design Highlights
* Explicit State Pattern for game phases 
* Command Pattern for hero and exploration actions 
* Centralized LoVBoard as the single source of truth 
* Deterministic combat resolution 
* Guaranteed lane traversability 
* Clean separation between exploration and combat logic

See Design  document for architectural details.

⸻

Notes
* All random board generation guarantees at least one traversable path per lane 
* Combat and exploration logic are fully decoupled 
* Designed for correctness and debuggability over visual complexity
⸻

Authors
* Akash Sivanandan
* Arnav Chaudhry
* Ankith Rao