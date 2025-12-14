package combat;

import combat.action.*;
import character.Hero;
import character.Monster;
import item.Spell;

import java.util.List;
import java.util.Scanner;

public class PlayerActionManager implements HeroActionManager {

    private final Scanner scanner;
    private final List<Monster> monsters;

    public PlayerActionManager(Scanner scanner, List<Monster> monsters) {
        this.scanner = scanner;
        this.monsters = monsters;
    }

    @Override
    public HeroAction nextAction(Hero hero) {
        while (true) {
            System.out.println("\nAction for " + hero.getName());
            System.out.println("1. Attack");
            System.out.println("2. Cast Spell");
            System.out.println("3. Skip");
            System.out.println("0. Quit");
            System.out.print("> ");

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "q":
                    return null;
                case "1":
                    return chooseAttack(hero);
                case "2":
                    return chooseSpell(hero);
                case "3":
                    return new SkipAction(hero);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

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
}