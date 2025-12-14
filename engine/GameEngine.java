package engine;

import character.*;
import item.*;
import util.DataLoader;
import world.mh.GameMap;
import combat.Battle;
import world.mh.Position;
import world.mh.*;

import java.util.*;

public class GameEngine {

    private static final int MAP_SIZE = 8;
    private static final double BATTLE_PROBABILITY = 0.3;

    private GameMap map;
    private Party party;
    private Market market;
    private Random random = new Random();

    // Prototypes loaded from files
    private List<Warrior> warriorPrototypes;
    private List<Paladin> paladinPrototypes;
    private List<Sorcerer> sorcererPrototypes;
    private List<Weapon> weaponPrototypes;
    private List<Armor> armorPrototypes;
    private List<Potion> potionPrototypes;
    private List<Spell> fireSpellPrototypes;
    private List<Spell> iceSpellPrototypes;
    private List<Spell> lightningSpellPrototypes;

    public void start() {
        System.out.println("Welcome to Legends: Monsters and Heroes!");
        System.out.println("----------------------------------------------------");

        initData();

        try (Scanner scanner = new Scanner(System.in)) {
            setupParty(scanner);
            map = new GameMap(MAP_SIZE);
            // Ensure starting tile is accessible & not inaccessible
            map.setHeroPosition(0, 0);
            Hero hero = party.getHeroes().get(0);
            hero.setPosition(new Position(0, 0, 0));

            gameLoop(scanner);
        }

        System.out.println("Thanks for playing!");
    }

    private void initData() {
        warriorPrototypes = DataLoader.loadWarriors();
        paladinPrototypes = DataLoader.loadPaladins();
        sorcererPrototypes = DataLoader.loadSorcerers();

        weaponPrototypes = DataLoader.loadWeapons();
        armorPrototypes = DataLoader.loadArmors();
        potionPrototypes = DataLoader.loadPotions();
        fireSpellPrototypes = DataLoader.loadFireSpells();
        iceSpellPrototypes = DataLoader.loadIceSpells();
        lightningSpellPrototypes = DataLoader.loadLightningSpells();

        market = new Market(weaponPrototypes, armorPrototypes, potionPrototypes,
                            fireSpellPrototypes, iceSpellPrototypes, lightningSpellPrototypes);
    }

    private void setupParty(Scanner scanner) {
        System.out.println("Create your party (1 - 3 heroes).");

        List<Hero> choices = new ArrayList<>();
        choices.addAll(warriorPrototypes);
        choices.addAll(paladinPrototypes);
        choices.addAll(sorcererPrototypes);

        if (choices.isEmpty()) {
            System.out.println("No heroes available from data files!");
            System.exit(1);
        }

        int partySize = 0;
        while (partySize < 1 || partySize > 3) {
            System.out.print("How many heroes in your party (1-3)? ");
            try {
                partySize = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                partySize = 0;
            }
        }

        List<Hero> selected = new ArrayList<>();
        for (int i = 0; i < partySize; i++) {
            System.out.println("\nChoose hero #" + (i + 1));
            printHeroChoices(choices);

            int index = -1;
            while (index < 0 || index >= choices.size()) {
                System.out.print("Enter index of hero: ");
                try {
                    index = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    index = -1;
                }
            }

            Hero prototype = choices.get(index);
            Hero copy = prototype.createCopy(); // deep copy
            selected.add(copy);
            System.out.println("Added " + copy.getName() + " to your party.");
        }

        party = new Party(selected);
    }

    private void printHeroChoices(List<Hero> choices) {
        for (int i = 0; i < choices.size(); i++) {
            Hero h = choices.get(i);
            System.out.printf("[%d] %-20s (%s) Lvl:%d HP:%d MP:%d STR:%d DEX:%d AGI:%d GOLD:%d EXP:%d%n",
                    i,
                    h.getName(),
                    h.getClass().getSimpleName(),
                    h.getLevel(),
                    h.getHp(),
                    h.getMp(),
                    h.getStrength(),
                    h.getDexterity(),
                    h.getAgility(),
                    h.getGold(),
                    h.getExperience());
        }
    }

    private void gameLoop(Scanner scanner) {
        boolean playing = true;
    
        printInstructions();
    
        while (playing) {
            System.out.println("\n=== World Map ===");
            map.printMap();
    
            System.out.println("\ncharacter.Party status:");
            party.printSummary();
    
            System.out.print(
                "\nCommand (W/A/S/D move, M market, I info, E equip, P potion, H help, Q quit): "
            );
            String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
    
            if (input.isEmpty()) {
                continue;
            }
    
            char cmd = input.charAt(0);
    
            switch (cmd) {
                case 'W':
                case 'A':
                case 'S':
                case 'D':
                    handleMove(cmd, scanner);
                    break;
                case 'M':
                    handleMarket(scanner);
                    break;
                case 'I':
                    showHeroesInfo();
                    break;
                case 'E':
                    handleEquipWorld(scanner);
                    break;
                case 'P':
                    handleUsePotionWorld(scanner);
                    break;
                case 'H':
                    printInstructions();
                    break;
                case 'Q':
                    System.out.println("Quitting game...");
                    playing = false;
                    break;
                default:
                    System.out.println("Unknown command. Press H for help.");
            }
    
            if (!party.hasAliveHero()) {
                System.out.println("All your heroes have fainted. Game over.");
                playing = false;
            }
        }
    }

    private void handleEquipWorld(Scanner scanner) {
        System.out.println("\n== Change Equipment ==");
        List<Hero> heroes = party.getHeroes();
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            System.out.printf("[%d] %s%n", i, h.getName());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose hero: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;
    
        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= heroes.size()) {
            System.out.println("Invalid hero index.");
            return;
        }
    
        Hero hero = heroes.get(idx);
        Inventory inv = hero.getInventory();
        System.out.println("Change equipment for " + hero.getName());
        System.out.println("1. Equip weapon");
        System.out.println("2. Equip armor");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();
    
        switch (choice) {
            case "1":
                List<Weapon> weapons = inv.getWeapons();
                if (weapons.isEmpty()) {
                    System.out.println("No weapons available.");
                    return;
                }
                System.out.println("Weapons:");
                for (int i = 0; i < weapons.size(); i++) {
                    Weapon w = weapons.get(i);
                    System.out.printf("[%d] %s (Dmg:%d, Hands:%d)%n",
                            i, w.getName(), w.getDamage(), w.getHandsRequired());
                }
                System.out.print("Choose weapon index: ");
                String inW = scanner.nextLine().trim();
                try {
                    int idxW = Integer.parseInt(inW);
                    if (idxW < 0 || idxW >= weapons.size()) {
                        System.out.println("Invalid index.");
                        return;
                    }
                    inv.equipWeapon(idxW);
                    System.out.println("Equipped " + weapons.get(idxW).getName());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
                break;
    
            case "2":
                List<Armor> armors = inv.getArmors();
                if (armors.isEmpty()) {
                    System.out.println("No armors available.");
                    return;
                }
                System.out.println("Armors:");
                for (int i = 0; i < armors.size(); i++) {
                    Armor a = armors.get(i);
                    System.out.printf("[%d] %s (Red:%d)%n",
                            i, a.getName(), a.getDamageReduction());
                }
                System.out.print("Choose armor index: ");
                String inA = scanner.nextLine().trim();
                try {
                    int idxA = Integer.parseInt(inA);
                    if (idxA < 0 || idxA >= armors.size()) {
                        System.out.println("Invalid index.");
                        return;
                    }
                    inv.equipArmor(idxA);
                    System.out.println("Equipped " + armors.get(idxA).getName());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
                break;
    
            case "3":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void handleUsePotionWorld(Scanner scanner) {
        System.out.println("\n== Use item.Potion ==");
        List<Hero> heroes = party.getHeroes();
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            System.out.printf("[%d] %s%n", i, h.getName());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose hero: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;
    
        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= heroes.size()) {
            System.out.println("Invalid hero index.");
            return;
        }
    
        Hero hero = heroes.get(idx);
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions available for " + hero.getName());
            return;
        }
    
        System.out.println("Potions for " + hero.getName() + ":");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("[%d] %s (Increase:%d)%n",
                    i, p.getName(), p.getAttributeIncrease());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose potion index: ");
        String potIn = scanner.nextLine().trim();
        if (potIn.equalsIgnoreCase("x")) return;
    
        int pIdx;
        try {
            pIdx = Integer.parseInt(potIn);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
    
        if (pIdx < 0 || pIdx >= potions.size()) {
            System.out.println("Invalid index.");
            return;
        }
    
        Potion potion = potions.remove(pIdx);
        potion.applyTo(hero);
        System.out.println("Used potion " + potion.getName() + " on " + hero.getName());
    }
    
    

    private void handleMove(char cmd, Scanner scanner) {
        int row = map.getHeroRow();
        int col = map.getHeroCol();

        switch (cmd) {
            case 'W': row -= 1; break;
            case 'S': row += 1; break;
            case 'A': col -= 1; break;
            case 'D': col += 1; break;
        }

        if (!map.isWithinBounds(row, col)) {
            System.out.println("You can't move outside the map!");
            return;
        }

        Tile target = map.getTile(row, col);
        if (!target.isAccessible()) {
            System.out.println("That tile is inaccessible.");
            return;
        }

        map.setHeroPosition(row, col);
        Hero hero = party.getHeroes().get(0);
        hero.setPosition(new Position(row, col, 0));

        if (target instanceof MarketTile) {
            System.out.println("You entered a engine.Market tile! Press M to trade.");
        } else if (target instanceof CommonTile) {
            // Chance of battle
            if (random.nextDouble() < BATTLE_PROBABILITY) {
                System.out.println("A group of monsters appears!");
                startBattle(scanner);
            }
        }
    }

    private void startBattle(Scanner scanner) {
        int heroCount = party.getHeroes().size();
        int maxHeroLevel = party.getMaxLevel();

        List<Monster> monsters = DataLoader.generateRandomMonsters(heroCount, maxHeroLevel);

        Battle battle = new Battle();
        boolean heroesWin = battle.execute(party, monsters, scanner);

        if (!heroesWin) {
            System.out.println("Your party was defeated...");
        }
    }

    private void handleMarket(Scanner scanner) {
        Tile tile = map.getTile(map.getHeroRow(), map.getHeroCol());
        if (!(tile instanceof MarketTile)) {
            System.out.println("You are not standing on a market tile.");
            return;
        }

        System.out.println("\nYou entered a market.");
        while (true) {
            System.out.println("\nChoose hero to trade for:");
            List<Hero> heroes = party.getHeroes();
            for (int i = 0; i < heroes.size(); i++) {
                System.out.printf("[%d] %s (Gold: %d)%n", i, heroes.get(i).getName(), heroes.get(i).getGold());
            }
            System.out.println("[X] Exit market");

            String in = scanner.nextLine().trim();
            if (in.equalsIgnoreCase("x")) {
                System.out.println("Leaving market.");
                return;
            }

            int idx;
            try {
                idx = Integer.parseInt(in);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (idx < 0 || idx >= heroes.size()) {
                System.out.println("Invalid hero index.");
                continue;
            }

            Hero h = heroes.get(idx);
            market.interact(h, scanner);
        }
    }

    private void showHeroesInfo() {
        System.out.println("\n=== HEROES INFO ===");
        for (Hero h : party.getHeroes()) {
            System.out.println(h.detailedInfo());
            System.out.println("------------------------------");
        }
    }

    private void printInstructions() {
        System.out.println("\n=== Instructions ===");
        System.out.println("Move with W (up), A (left), S (down), D (right).");
        System.out.println("Common tiles may trigger battles.");
        System.out.println("engine.Market tiles let you buy/sell items (press M).");
        System.out.println("I: Show hero information");
        System.out.println("E: Change equipment (outside battle)");
        System.out.println("P: Use a potion (outside battle)");
        System.out.println("H: Show this help");
        System.out.println("Q: Quit the game (works anywhere)");
        System.out.println("In battle: choose actions for each hero (attack, spell, potion, equip, info).");
    }
    
}
