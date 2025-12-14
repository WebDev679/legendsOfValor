package combat.test;

import character.*;
import combat.*;
import item.Inventory;
import item.Potion;
import item.Spell;
import item.SpellType;
import world.Position;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

import static item.AttributeType.HEALTH;

/**
 * Smoke test for validating core battle loop.
 *
 * <p>This class is used for manual and automated testing of combat flow,
 * hero actions, and monster AI behavior.
 */

public class ValorBattleSmokeTest {

    public static void main(String[] args) {
        System.out.println("===== VALOR BATTLE SMOKE TEST START =====");

        Hero hero = new Warrior(
                "Bilbo Baggins",
                100,
                30,
                10,
                10,
                0,
                0
        );
        hero.setPosition(new Position(0, 0, 0));

        Inventory inv = hero.getInventory();
        inv.addSpell(new Spell(
                "Fireball",
                0,
                1,
                50,
                30,
                SpellType.FIRE
        ));
        inv.addPotion(
                new Potion(
                        "Potion of healing",
                        0,
                        0,
                        5,
                        EnumSet.of(HEALTH)
                )
        );

        Monster monster = new Dragon(
                "Smaug",
                1,
                30,
                10,
                0.2
        );
        monster.setPosition(new Position(0, 1, 0)); // adjacent

        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        List<Monster> monsters = new ArrayList<>();
        monsters.add(monster);

        Scanner scanner = new Scanner(System.in);
        HeroActionManager actionManager =
                new AutoAction(monsters);
//        HeroActionManager actionManager =
//                new PlayerActionManager(scanner, monsters);

        ValorBattle battle = new ValorBattle();

        int round = 1;

        try {
            while (hasAliveHero(heroes) && hasAliveMonster(monsters)) {
                System.out.println("\n===== ROUND " + round + " =====");

                battle.resolveRound(
                        heroes,
                        monsters,
                        actionManager
                );

                round++;
            }
        } catch (QuitBattleException e) {
            System.out.println("Player quit battle.");
        }

        System.out.println("\n===== BATTLE OVER =====");
        System.out.println("Heroes alive? " + hasAliveHero(heroes));
    }

    private static boolean hasAliveHero(List<Hero> heroes) {
        for (Hero h : heroes) {
            if (h.isAlive()) return true;
        }
        return false;
    }

    private static boolean hasAliveMonster(List<Monster> monsters) {
        for (Monster m : monsters) {
            if (m.isAlive()) return true;
        }
        return false;
    }
}