package state;

import character.Hero;
import engine.MarketService;
import item.Armor;
import item.Artifact;
import item.Item;
import item.Potion;
import item.Spell;
import item.Weapon;
import util.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Legends of Valor market state.
 *
 * <p>This state represents a heroes' Nexus market. It uses {@link MarketService}
 * to apply buy/sell rules and exposes a simple text UI for selecting a hero and
 * trading items. The state assumes that {@link GameContext#isHeroOnNexus(Hero)}
 * will be implemented once the full board and Nexus logic are in place.</p>
 */
public class MarketState implements GameState {

    private final GameContext context;
    private final StateManager stateManager;
    private final Scanner scanner = new Scanner(System.in);

    public MarketState(GameContext context, StateManager stateManager) {
        this.context = context;
        this.stateManager = stateManager;
    }

    @Override
    public void enter() {
        ensureMarketServiceInitialised();

        if (!context.anyHeroOnNexus()) {
            System.out.println("No hero is currently standing on a Nexus. Market is unavailable.");
            stateManager.changeState(new ExplorationState(context, stateManager));
            return;
        }

        System.out.println("\n=== Legends of Valor Nexus Market ===");
    }

    @Override
    public void update() {
        if (!context.anyHeroOnNexus()) {
            System.out.println("Heroes have left the Nexus. Leaving market.");
            stateManager.changeState(new ExplorationState(context, stateManager));
            return;
        }

        runMarketLoop();
        stateManager.changeState(new ExplorationState(context, stateManager));
    }

    @Override
    public void exit() {
        // nothing to clean up for now
    }

    /**
     * Lazily initialises the shared MarketService with item prototypes.
     * This keeps item loading in one place for LoV.
     */
    private void ensureMarketServiceInitialised() {
        if (context.marketService != null) {
            return;
        }

        List<Weapon> weapons = DataLoader.loadWeapons();
        List<Armor> armors = DataLoader.loadArmors();
        List<Potion> potions = DataLoader.loadPotions();

        List<Spell> allSpells = new ArrayList<Spell>();
        allSpells.addAll(DataLoader.loadFireSpells());
        allSpells.addAll(DataLoader.loadIceSpells());
        allSpells.addAll(DataLoader.loadLightningSpells());

        context.marketService = new MarketService(
                weapons,
                armors,
                potions,
                allSpells,
                new ArrayList<Artifact>()   // no custom artifacts yet
        );
    }

    private void runMarketLoop() {
        boolean inMarket = true;
        while (inMarket && context.gameRunning && context.anyHeroOnNexus()) {
            System.out.println("\nSelect hero to trade for:");
            for (int i = 0; i < context.heroes.size(); i++) {
                Hero h = context.heroes.get(i);
                System.out.printf("[%d] %s (Gold: %d, Lvl: %d)%n",
                        i, h.getName(), h.getGold(), h.getLevel());
            }
            System.out.println("[X] Exit market");
            System.out.print("> ");

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("x")) {
                System.out.println("Leaving Nexus market.");
                inMarket = false;
                break;
            }
            if (input.equalsIgnoreCase("q")) {
                context.gameRunning = false;
                stateManager.changeState(new GameOverState(context));
                return;
            }

            int idx;
            try {
                idx = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (idx < 0 || idx >= context.heroes.size()) {
                System.out.println("Invalid hero index.");
                continue;
            }

            Hero hero = context.heroes.get(idx);
            heroMarketMenu(hero);
        }
    }

    private void heroMarketMenu(Hero hero) {
        boolean forHero = true;
        while (forHero && context.gameRunning && context.anyHeroOnNexus()) {
            System.out.println("\n=== Market for " + hero.getName() + " (Gold: " + hero.getGold() + ") ===");
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Back");
            System.out.print("> ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    buyMenu(hero);
                    break;
                case "2":
                    sellMenu(hero);
                    break;
                case "3":
                    forHero = false;
                    break;
                case "q":
                case "Q":
                    context.gameRunning = false;
                    stateManager.changeState(new GameOverState(context));
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ===== BUY side =====

    private void buyMenu(Hero hero) {
        System.out.println("\nBuy Menu:");
        System.out.println("1. Weapons");
        System.out.println("2. Armors");
        System.out.println("3. Potions");
        System.out.println("4. Spells");
        System.out.println("5. Artifacts");
        System.out.println("6. Back");
        System.out.print("Choose: ");

        String input = scanner.nextLine().trim();
        switch (input) {
            case "1":
                buyWeapon(hero);
                break;
            case "2":
                buyArmor(hero);
                break;
            case "3":
                buyPotion(hero);
                break;
            case "4":
                buySpell(hero);
                break;
            case "5":
                buyArtifact(hero);
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void buyWeapon(Hero hero) {
        List<Weapon> weapons = context.marketService.getWeaponPrototypes();
        if (weapons.isEmpty()) {
            System.out.println("No weapons available.");
            return;
        }
        System.out.println("\nWeapons for sale:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.printf("[%d] %s (Price:%d, Lvl:%d, Dmg:%d, Hands:%d)%n",
                    i, w.getName(), w.getPrice(), w.getRequiredLevel(), w.getDamage(), w.getHandsRequired());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose weapon index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= weapons.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Weapon proto = weapons.get(idx);
        if (!context.marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this weapon.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this weapon right now.");
            }
            return;
        }

        context.marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buyArmor(Hero hero) {
        List<Armor> armors = context.marketService.getArmorPrototypes();
        if (armors.isEmpty()) {
            System.out.println("No armors available.");
            return;
        }
        System.out.println("\nArmors for sale:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.printf("[%d] %s (Price:%d, Lvl:%d, Red:%d)%n",
                    i, a.getName(), a.getPrice(), a.getRequiredLevel(), a.getDamageReduction());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose armor index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= armors.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Armor proto = armors.get(idx);
        if (!context.marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this armor.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this armor right now.");
            }
            return;
        }

        context.marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buyPotion(Hero hero) {
        List<Potion> potions = context.marketService.getPotionPrototypes();
        if (potions.isEmpty()) {
            System.out.println("No potions available.");
            return;
        }
        System.out.println("\nPotions for sale:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("[%d] %s (Price:%d, Lvl:%d, Increase:%d)%n",
                    i, p.getName(), p.getPrice(), p.getRequiredLevel(), p.getAttributeIncrease());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose potion index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= potions.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Potion proto = potions.get(idx);
        if (!context.marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this potion.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this potion right now.");
            }
            return;
        }

        context.marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buySpell(Hero hero) {
        List<Spell> spells = context.marketService.getSpellPrototypes();
        if (spells.isEmpty()) {
            System.out.println("No spells available.");
            return;
        }
        System.out.println("\nSpells for sale:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("[%d] %s (%s, Price:%d, Lvl:%d, Dmg:%d, MP:%d)%n",
                    i, s.getName(), s.getType(), s.getPrice(), s.getRequiredLevel(), s.getDamage(), s.getManaCost());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose spell index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= spells.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Spell proto = spells.get(idx);
        if (!context.marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this spell.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this spell right now.");
            }
            return;
        }

        context.marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buyArtifact(Hero hero) {
        List<Artifact> artifacts = context.marketService.getArtifactPrototypes();
        if (artifacts.isEmpty()) {
            System.out.println("No artifacts available.");
            return;
        }
        System.out.println("\nArtifacts for sale:");
        for (int i = 0; i < artifacts.size(); i++) {
            Artifact a = artifacts.get(i);
            System.out.printf("[%d] %s (Price:%d, Lvl:%d)%n",
                    i, a.getName(), a.getPrice(), a.getRequiredLevel());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose artifact index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= artifacts.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Artifact proto = artifacts.get(idx);
        if (!context.marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this artifact.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this artifact right now.");
            }
            return;
        }

        context.marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    // ===== SELL side =====

    private void sellMenu(Hero hero) {
        System.out.println("\nSell Menu:");
        System.out.println("1. Weapons");
        System.out.println("2. Armors");
        System.out.println("3. Potions");
        System.out.println("4. Spells");
        System.out.println("5. Artifacts");
        System.out.println("6. Back");
        System.out.print("Choose: ");
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                sellWeapons(hero);
                break;
            case "2":
                sellArmors(hero);
                break;
            case "3":
                sellPotions(hero);
                break;
            case "4":
                sellSpells(hero);
                break;
            case "5":
                sellArtifacts(hero);
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void sellWeapons(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();
        if (weapons.isEmpty()) {
            System.out.println("No weapons to sell.");
            return;
        }
        System.out.println("\nYour weapons:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.printf("[%d] %s (Price:%d)%n", i, w.getName(), w.getPrice());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose weapon index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= weapons.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Weapon w = weapons.get(idx);
        if (!context.marketService.canSell(hero, w)) {
            System.out.println("You cannot sell this weapon.");
            return;
        }

        context.marketService.sell(hero, w);
        System.out.println("Sold " + w.getName() + " for " + (w.getPrice() / 2) + " gold.");
    }

    private void sellArmors(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmors();
        if (armors.isEmpty()) {
            System.out.println("No armors to sell.");
            return;
        }
        System.out.println("\nYour armors:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.printf("[%d] %s (Price:%d)%n", i, a.getName(), a.getPrice());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose armor index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= armors.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Armor a = armors.get(idx);
        if (!context.marketService.canSell(hero, a)) {
            System.out.println("You cannot sell this armor.");
            return;
        }

        context.marketService.sell(hero, a);
        System.out.println("Sold " + a.getName() + " for " + (a.getPrice() / 2) + " gold.");
    }

    private void sellPotions(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions to sell.");
            return;
        }
        System.out.println("\nYour potions:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("[%d] %s (Price:%d)%n", i, p.getName(), p.getPrice());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose potion index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= potions.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Potion p = potions.get(idx);
        if (!context.marketService.canSell(hero, p)) {
            System.out.println("You cannot sell this potion.");
            return;
        }

        context.marketService.sell(hero, p);
        System.out.println("Sold " + p.getName() + " for " + (p.getPrice() / 2) + " gold.");
    }

    private void sellSpells(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();
        if (spells.isEmpty()) {
            System.out.println("No spells to sell.");
            return;
        }
        System.out.println("\nYour spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("[%d] %s (Price:%d)%n", i, s.getName(), s.getPrice());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose spell index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= spells.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Spell s = spells.get(idx);
        if (!context.marketService.canSell(hero, s)) {
            System.out.println("You cannot sell this spell.");
            return;
        }

        context.marketService.sell(hero, s);
        System.out.println("Sold " + s.getName() + " for " + (s.getPrice() / 2) + " gold.");
    }

    private void sellArtifacts(Hero hero) {
        List<Artifact> artifacts = hero.getInventory().getArtifacts();
        if (artifacts.isEmpty()) {
            System.out.println("No artifacts to sell.");
            return;
        }
        System.out.println("\nYour artifacts:");
        for (int i = 0; i < artifacts.size(); i++) {
            Artifact a = artifacts.get(i);
            System.out.printf("[%d] %s (Price:%d)%n", i, a.getName(), a.getPrice());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose artifact index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= artifacts.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Artifact a = artifacts.get(idx);
        if (!context.marketService.canSell(hero, a)) {
            System.out.println("You cannot sell this artifact.");
            return;
        }

        context.marketService.sell(hero, a);
        System.out.println("Sold " + a.getName() + " for " + (a.getPrice() / 2) + " gold.");
    }
}