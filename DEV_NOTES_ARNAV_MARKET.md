Legends of Valor – Market & Items Work (Arnav)
==============================================

Scope for this branch:

- Refine item and inventory APIs so they are reusable across both game modes.
- Introduce a MarketService that owns buy/sell rules without doing any I/O.
- Keep legacy Monsters & Heroes behaviour intact while preparing a clean path
  for Legends of Valor nexus-based markets.

Initial milestones:

1. Item & inventory cleanup (helpers, ItemCategory, non‑UI logic).
2. MarketService that encapsulates buying/selling rules and operates on Hero
   and Inventory objects only.
3. Later: refactor existing engine.Market and add a Legends of Valor market
   flow to consume MarketService.


