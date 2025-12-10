# CS611-Assignment
## Monsters and Heroes 
---------------------------------------------------------------------------
Name: Arnav Pratap Chaudhry
Email: arnavpc@bu.edu
Student ID: U20869212

## Files
---------------------------------------------------------------------------
Java Source Files
LegendsGame.java: Entry point (main) and GameEngine, manages game setup, main loop, and high-level input handling (movement, market access, inventory, quit).
GameMap.java: Generates and maintains the square world map with Tile types (Common, Market, Inaccessible) and the hero’s position.
Character.java: Abstract Character plus Hero and Monster hierarchies. Defines Warrior, Paladin, Sorcerer, Dragon, Exoskeleton, Spirit, and the Party class.
Item.java: Abstract Item plus concrete Weapon, Armor, Potion, Spell, AttributeType, SpellType, and Inventory management (equipping, listing, and storing items).
Market.java: Implements the Market logic for buying/selling weapons, armors, potions, and spells with level and gold checks per hero.
Battle.java: Orchestrates turn-based combat between a Party of heroes and a list of monsters, including attacks, spells, potions, equipment changes, regen, victory/defeat handling, and rewards.
DataLoader.java: Loads all hero, monster, item, and spell prototypes from the provided .txt configuration files and generates balanced random monster parties for battles.

## Notes
---------------------------------------------------------------------------
- Clean separation of concerns across map, game engine, characters, items, markets, and battles.
- Heroes and monsters share base Character behavior while subclasses specialize stats and progression.
- New hero/monster types or items can be added by extending the existing class hierarchies and/or adding new data files.
- The GameMap, Market, and Battle classes are decoupled so alternative UIs or features (e.g., different maps, GUIs) can be added with minimal changes.

## How to compile and run
---------------------------------------------------------------------------
Environment: Java 8 required. Tested via terminal 

1) Verify Java version
   java -version
   # expect something like: java version "1.8.0_xxx"

2) Compile (from the directory containing the .java files)
   javac *.java

3) Run
   java LegendsGame

## Input/Output Example
---------------------------------------------------------------------------
Welcome to Legends: Monsters and Heroes!
----------------------------------------------------
Create your party (1 - 3 heroes).
How many heroes in your party (1-3)? 1

Choose hero #1
[0] Gaerdal_Ironhand     (Warrior) Lvl:1 HP:100 MP:100 STR:700 DEX:600 AGI:500 GOLD:1354 EXP:7
[1] Sehanine_Monnbow     (Warrior) Lvl:1 HP:100 MP:600 STR:700 DEX:500 AGI:800 GOLD:2500 EXP:8
[2] Muamman_Duathall     (Warrior) Lvl:1 HP:100 MP:300 STR:900 DEX:750 AGI:500 GOLD:2546 EXP:6
[3] Flandal_Steelskin    (Warrior) Lvl:1 HP:100 MP:200 STR:750 DEX:700 AGI:650 GOLD:2500 EXP:7
[4] Undefeated_Yoj       (Warrior) Lvl:1 HP:100 MP:400 STR:800 DEX:700 AGI:400 GOLD:2500 EXP:7
[5] Eunoia_Cyn           (Warrior) Lvl:1 HP:100 MP:400 STR:700 DEX:600 AGI:800 GOLD:2500 EXP:6
[6] Parzival             (Paladin) Lvl:1 HP:100 MP:300 STR:750 DEX:700 AGI:650 GOLD:2500 EXP:7
[7] Sehanine_Moonbow     (Paladin) Lvl:1 HP:100 MP:300 STR:750 DEX:700 AGI:700 GOLD:2500 EXP:7
[8] Skoraeus_Stonebones  (Paladin) Lvl:1 HP:100 MP:250 STR:650 DEX:350 AGI:600 GOLD:2500 EXP:4
[9] Garl_Glittergold     (Paladin) Lvl:1 HP:100 MP:100 STR:600 DEX:400 AGI:500 GOLD:2500 EXP:5
[10] Amaryllis_Astra      (Paladin) Lvl:1 HP:100 MP:500 STR:500 DEX:500 AGI:500 GOLD:2500 EXP:5
[11] Caliber_Heist        (Paladin) Lvl:1 HP:100 MP:400 STR:400 DEX:400 AGI:400 GOLD:2500 EXP:8
[12] Rillifane_Rallathil  (Sorcerer) Lvl:1 HP:100 MP:1300 STR:750 DEX:500 AGI:450 GOLD:2500 EXP:9
[13] Segojan_Earthcaller  (Sorcerer) Lvl:1 HP:100 MP:900 STR:800 DEX:650 AGI:500 GOLD:2500 EXP:5
[14] Reign_Havoc          (Sorcerer) Lvl:1 HP:100 MP:800 STR:800 DEX:800 AGI:800 GOLD:2500 EXP:8
[15] Reverie_Ashels       (Sorcerer) Lvl:1 HP:100 MP:900 STR:800 DEX:400 AGI:700 GOLD:2500 EXP:7
[16] Kalabar              (Sorcerer) Lvl:1 HP:100 MP:800 STR:850 DEX:600 AGI:400 GOLD:2500 EXP:6
[17] Skye_Soar            (Sorcerer) Lvl:1 HP:100 MP:1000 STR:700 DEX:500 AGI:400 GOLD:2500 EXP:5
Enter index of hero: 14
Added Reign_Havoc to your party.

=== Instructions ===
Move with W (up), A (left), S (down), D (right).
Common tiles may trigger battles.
Market tiles let you buy/sell items (press M).
I: Show hero information
E: Change equipment (outside battle)
P: Use a potion (outside battle)
H: Show this help
Q: Quit the game (works anywhere)
In battle: choose actions for each hero (attack, spell, potion, equip, info).

=== World Map ===
H  C  M  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2500

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): W
You can't move outside the map!

=== World Map ===
H  C  M  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2500

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): D

=== World Map ===
C  H  M  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2500

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): M
You are not standing on a market tile.

=== World Map ===
C  H  M  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2500

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): D
You entered a Market tile! Press M to trade.

=== World Map ===
C  C  H  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2500

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): M

You entered a market.

Choose hero to trade for:
[0] Reign_Havoc (Gold: 2500)
[X] Exit market
0

=== Market ===
Hero: Reign_Havoc (Gold: 2500, Level: 1)
1. Buy
2. Sell
3. Exit Market
Choose: 1

Buy Menu:
1. Weapons
2. Armors
3. Potions
4. Spells
5. Back
Choose: 2

Armors for sale:
[0] Platinum_Shield (Price:150, Lvl:1, Red:200)
[1] Breastplate (Price:350, Lvl:3, Red:600)
[2] Full_Body_Armor (Price:1000, Lvl:8, Red:1100)
[3] Wizard_Shield (Price:1200, Lvl:10, Red:1500)
[4] Guardian_Angel (Price:1000, Lvl:10, Red:1000)
[X] Cancel
Choose armor index: 0
You purchased Platinum_Shield

=== Market ===
Hero: Reign_Havoc (Gold: 2350, Level: 1)
1. Buy
2. Sell
3. Exit Market
Choose: 3

Choose hero to trade for:
[0] Reign_Havoc (Gold: 2350)
[X] Exit market
x
Leaving market.

=== World Map ===
C  C  H  C  M  X  X  C
M  M  C  C  C  C  M  C
M  C  C  M  M  M  C  M
C  C  C  M  C  M  X  C
C  C  C  M  C  M  C  C
C  M  C  X  X  C  M  C
M  X  X  M  C  M  C  X
X  C  C  C  C  C  M  M

Party status:
Reign_Havoc (Sorcerer) L:1 HP:100/100 MP:800/800 Gold:2350

Command (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): Q
Quitting game...
Thanks for playing!


