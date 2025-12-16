package combat;

import combat.action.*;
import character.Hero;
import character.Monster;
import item.Armor;
import item.Potion;
import item.Spell;
import item.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles player controlled hero actions during combat
 *
 * <p>This implementation of {@link HeroActionManager} gives an interactive UI for users to
 * choose an action for each hero via standard input. </p>
 *
 * <p>The manager translates input into concrete {@link HeroAction} objects,
 * which then get resolved by combat system. This decouples user interaction from battle execution flow.</p>
 *
 * <p>Supporte actions include attacking, potion use, spellcasting, skipping turn and quitting battle</p>
 */
public class PlayerActionManager implements HeroActionManager {

    private final Scanner scanner;
    private final List<Monster> monsters;

    /**
     * Construsts new {@code PlayerActionManager}
     * @param scanner scanner used to read input
     * @param monsters monster list participating in battle
     */
    public PlayerActionManager(Scanner scanner, List<Monster> monsters) {
        this.scanner = scanner;
        this.monsters = monsters;
    }

    /**
     * Prompt player to choose action for given hero
     * <p>This is a blocking method until a valid action is selected. Invalid input
     * casues the menu to be displayed again</p>
     * @param hero hero whose turn is being processed
     * @return concrete {@link HeroAction} representing player's choice of action
     */
    @Override
    public HeroAction nextAction(Hero hero) {
        while (true) {
            System.out.println("\nAction for " + hero.getName());
            System.out.println("1. Attack");
            System.out.println("2. Cast Spell");
            System.out.println("3. Use Potion");
            System.out.println("4. Change Equipment");
            System.out.println("5. Skip");
            System.out.println("0. Quit");
            System.out.print("> ");

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "q":
                    return new QuitAction();
                case "1":
                    return chooseAttack(hero);
                case "2":
                    return chooseSpell(hero);
                case "3":
                    HeroAction action = choosePotion(hero);
                    if (action != null) return action;
                    break;
                case "4":
                    HeroAction equipAction = chooseEquipment(hero);
                    if (equipAction != null) return equipAction;
                    break;
                case "5":
                    return new SkipAction(hero);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Prompt player to select and use potion from inventory
     * @param hero hero using potion
     * @return a {@link PotionAction} or {@code null} if selection becomes a noop
     */
    private HeroAction choosePotion(Hero hero) {
        List <Potion> potions = new ArrayList<>();
        potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("No potions found.");
            return null;
        }

        for (int i = 0; i<potions.size(); i++) {
            System.out.println((i) + ". " + potions.get(i).getName());
        }
        try{
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return new PotionAction(hero, potions.get(choice));
        } catch (Exception e){
            System.out.println("Invalid choice.");
            return null;
        }
    }

    /**
     * Prompt player into choosing target to attack
     * @param hero attacking hero
     * @return a {@link AttackAction} or {@link SkipAction} on invalid input
     */
    private HeroAction chooseAttack(Hero hero) {
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.isAlive()) {
                System.out.println(i + ": " + m.getName());
            }
        }

        System.out.print("Choose target: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            Monster target = monsters.get(idx);
            return new AttackAction(hero, target);
        } catch (Exception e) {
            System.out.println("Invalid target.");
            return new SkipAction(hero);
        }
    }


    /**
     * Prompt player to select spell and target monster
     * @param hero spellcasting hero
     * @return a {@link SpellcastAction} or {@link SkipAction} on failure
     */
    private HeroAction chooseSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println("No spells available.");
            return new SkipAction(hero);
        }

        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println(
                    i + ": " + s.getName() +
                            " (DMG " + s.getDamage() +
                            ", MP " + s.getManaCost() + ")"
            );
        }

        System.out.print("Choose spell: ");
        try {
            int sIdx = Integer.parseInt(scanner.nextLine());
            Spell spell = spells.get(sIdx);

            for (int i = 0; i < monsters.size(); i++) {
                Monster m = monsters.get(i);
                if (m.isAlive()) {
                    System.out.println(i + ": " + m.getName());
                }
            }


            System.out.print("Choose target: ");
            int mIdx = Integer.parseInt(scanner.nextLine());
            Monster target = monsters.get(mIdx);

            return new SpellcastAction(hero, spell, target);

        } catch (Exception e) {
            System.out.println("Invalid selection.");
            return new SkipAction(hero);
        }
    }

    /**
     * Prompt player to change equipped weapon or armor for the given hero.
     *
     * <p>Returns an {@link EquipAction} when a valid selection is made,
     * or {@code null} if the player cancels or input is invalid, in which
     * case the main action menu will be shown again.</p>
     */
    private HeroAction chooseEquipment(Hero hero) {
        System.out.println("\n== Change Equipment for " + hero.getName() + " ==");
        System.out.println("1. Equip weapon");
        System.out.println("2. Equip armor");
        System.out.println("3. Back");
        System.out.print("> ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                List<Weapon> weapons = hero.getInventory().getWeapons();
                if (weapons.isEmpty()) {
                    System.out.println("No weapons available.");
                    return null;
                }
                for (int i = 0; i < weapons.size(); i++) {
                    Weapon w = weapons.get(i);
                    System.out.printf(
                            "%d: %s (Dmg:%d, Hands:%d)%n",
                            i, w.getName(), w.getDamage(), w.getHandsRequired()
                    );
                }
                System.out.print("Choose weapon index: ");
                try {
                    int idx = Integer.parseInt(scanner.nextLine().trim());
                    if (idx < 0 || idx >= weapons.size()) {
                        System.out.println("Invalid index.");
                        return null;
                    }
                    return new EquipAction(hero, EquipAction.Slot.WEAPON, idx);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    return null;
                }
            case "2":
                List<Armor> armors = hero.getInventory().getArmors();
                if (armors.isEmpty()) {
                    System.out.println("No armors available.");
                    return null;
                }
                for (int i = 0; i < armors.size(); i++) {
                    Armor a = armors.get(i);
                    System.out.printf(
                            "%d: %s (Red:%d)%n",
                            i, a.getName(), a.getDamageReduction()
                    );
                }
                System.out.print("Choose armor index: ");
                try {
                    int idx = Integer.parseInt(scanner.nextLine().trim());
                    if (idx < 0 || idx >= armors.size()) {
                        System.out.println("Invalid index.");
                        return null;
                    }
                    return new EquipAction(hero, EquipAction.Slot.ARMOR, idx);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    return null;
                }
            case "3":
                return null;
            default:
                System.out.println("Invalid choice.");
                return null;
        }
    }
}