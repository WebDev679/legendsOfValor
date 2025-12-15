package engine;// engine.Market.java
import character.Hero;
import item.Armor;
import item.Potion;
import item.Spell;
import item.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Market {

    private final List<Weapon> weaponPrototypes;
    private final List<Armor> armorPrototypes;
    private final List<Potion> potionPrototypes;
    private final List<Spell> fireSpellPrototypes;
    private final List<Spell> iceSpellPrototypes;
    private final List<Spell> lightningSpellPrototypes;

    /** Shared marketplace rules, reused by different game modes. */
    private final MarketService marketService;

    public Market(List<Weapon> weapons, List<Armor> armors,
                  List<Potion> potions, List<Spell> fireSpells,
                  List<Spell> iceSpells, List<Spell> lightningSpells) {
        this.weaponPrototypes = weapons != null ? weapons : new ArrayList<>();
        this.armorPrototypes = armors != null ? armors : new ArrayList<>();
        this.potionPrototypes = potions != null ? potions : new ArrayList<>();
        this.fireSpellPrototypes = fireSpells != null ? fireSpells : new ArrayList<>();
        this.iceSpellPrototypes = iceSpells != null ? iceSpells : new ArrayList<>();
        this.lightningSpellPrototypes = lightningSpells != null ? lightningSpells : new ArrayList<>();

        List<Spell> allSpells = new ArrayList<>();
        allSpells.addAll(this.fireSpellPrototypes);
        allSpells.addAll(this.iceSpellPrototypes);
        allSpells.addAll(this.lightningSpellPrototypes);

        this.marketService = new MarketService(
                this.weaponPrototypes,
                this.armorPrototypes,
                this.potionPrototypes,
                allSpells,
                new ArrayList<>()
        );
    }

    public void interact(Hero hero, Scanner scanner) {
        boolean inMarket = true;
        while (inMarket) {
            System.out.println("\n=== engine.Market ===");
            System.out.println("character.Hero: " + hero.getName() + " (Gold: " + hero.getGold() + ", Level: " + hero.getLevel() + ")");
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Exit engine.Market");
            System.out.print("Choose: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Quitting game...");
                System.exit(0);
            }


            switch (input) {
                case "1":
                    buyMenu(hero, scanner);
                    break;
                case "2":
                    sellMenu(hero, scanner);
                    break;
                case "3":
                    inMarket = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void buyMenu(Hero hero, Scanner scanner) {
        System.out.println("\nBuy Menu:");
        System.out.println("1. Weapons");
        System.out.println("2. Armors");
        System.out.println("3. Potions");
        System.out.println("4. Spells");
        System.out.println("5. Artifacts");
        System.out.println("6. Back");
        System.out.print("Choose: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            System.out.println("Quitting game...");
            System.exit(0);
        }


        switch (input) {
            case "1":
                buyWeapon(hero, scanner);
                break;
            case "2":
                buyArmor(hero, scanner);
                break;
            case "3":
                buyPotion(hero, scanner);
                break;
            case "4":
                buySpell(hero, scanner);
                break;
            case "5":
                // No artifacts wired up for legacy mode yet; stub for future use.
                System.out.println("No artifacts available.");
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void buyWeapon(Hero hero, Scanner scanner) {
        if (weaponPrototypes.isEmpty()) {
            System.out.println("No weapons available.");
            return;
        }
        System.out.println("\nWeapons for sale:");
        for (int i = 0; i < weaponPrototypes.size(); i++) {
            Weapon w = weaponPrototypes.get(i);
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

        if (idx < 0 || idx >= weaponPrototypes.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Weapon proto = weaponPrototypes.get(idx);
        if (!marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this weapon.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this weapon right now.");
            }
            return;
        }

        marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buyArmor(Hero hero, Scanner scanner) {
        if (armorPrototypes.isEmpty()) {
            System.out.println("No armors available.");
            return;
        }
        System.out.println("\nArmors for sale:");
        for (int i = 0; i < armorPrototypes.size(); i++) {
            Armor a = armorPrototypes.get(i);
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

        if (idx < 0 || idx >= armorPrototypes.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Armor proto = armorPrototypes.get(idx);
        if (!marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this armor.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this armor right now.");
            }
            return;
        }

        marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buyPotion(Hero hero, Scanner scanner) {
        if (potionPrototypes.isEmpty()) {
            System.out.println("No potions available.");
            return;
        }
        System.out.println("\nPotions for sale:");
        for (int i = 0; i < potionPrototypes.size(); i++) {
            Potion p = potionPrototypes.get(i);
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

        if (idx < 0 || idx >= potionPrototypes.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Potion proto = potionPrototypes.get(idx);
        if (!marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this potion.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this potion right now.");
            }
            return;
        }

        marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void buySpell(Hero hero, Scanner scanner) {
        List<Spell> allSpells = new ArrayList<>();
        allSpells.addAll(fireSpellPrototypes);
        allSpells.addAll(iceSpellPrototypes);
        allSpells.addAll(lightningSpellPrototypes);

        if (allSpells.isEmpty()) {
            System.out.println("No spells available.");
            return;
        }

        System.out.println("\nSpells for sale:");
        for (int i = 0; i < allSpells.size(); i++) {
            Spell s = allSpells.get(i);
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

        if (idx < 0 || idx >= allSpells.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Spell proto = allSpells.get(idx);
        if (!marketService.canBuy(hero, proto)) {
            if (hero.getLevel() < proto.getRequiredLevel()) {
                System.out.println("Your level is too low for this spell.");
            } else if (hero.getGold() < proto.getPrice()) {
                System.out.println("You don't have enough gold.");
            } else {
                System.out.println("You cannot buy this spell right now.");
            }
            return;
        }

        marketService.buy(hero, proto);
        System.out.println("You purchased " + proto.getName());
    }

    private void sellMenu(Hero hero, Scanner scanner) {
        System.out.println("\nSell Menu:");
        System.out.println("1. Weapons");
        System.out.println("2. Armors");
        System.out.println("3. Potions");
        System.out.println("4. Spells");
        System.out.println("5. Back");
        System.out.print("Choose: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            System.out.println("Quitting game...");
            System.exit(0);
        }


        switch (input) {
            case "1":
                sellWeapons(hero, scanner);
                break;
            case "2":
                sellArmors(hero, scanner);
                break;
            case "3":
                sellPotions(hero, scanner);
                break;
            case "4":
                sellSpells(hero, scanner);
                break;
            case "5":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void sellWeapons(Hero hero, Scanner scanner) {
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
        int salePrice = w.getPrice() / 2;
        marketService.sell(hero, w);
        System.out.println("Sold " + w.getName() + " for " + salePrice + " gold.");
    }

    private void sellArmors(Hero hero, Scanner scanner) {
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
        int salePrice = a.getPrice() / 2;
        marketService.sell(hero, a);
        System.out.println("Sold " + a.getName() + " for " + salePrice + " gold.");
    }

    private void sellPotions(Hero hero, Scanner scanner) {
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
        int salePrice = p.getPrice() / 2;
        marketService.sell(hero, p);
        System.out.println("Sold " + p.getName() + " for " + salePrice + " gold.");
    }

    private void sellSpells(Hero hero, Scanner scanner) {
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
        int salePrice = s.getPrice() / 2;
        marketService.sell(hero, s);
        System.out.println("Sold " + s.getName() + " for " + salePrice + " gold.");
    }
}
