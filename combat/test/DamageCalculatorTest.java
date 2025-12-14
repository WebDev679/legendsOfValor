package combat.test;
import combat.DamageCalculator;
import character.*;
import item.*;

public class DamageCalculatorTest {

    public static void run() {
        testHeroPhysicalDamage();
        testUnarmedHeroDamage();
        testMonsterDamage();
        System.out.println("✓ DamageCalculatorTest passed");
    }
    public static void testHeroPhysicalDamage() {
        System.out.println("Running DamageCalculatorTest");
        Hero hero = new Warrior(
                "Bilbo Baggins",
                50,
                20,
                10,
                10,
                0,
                0
        );

        Monster monster = new Dragon(
                "Smaug",
                1,
                30,
                10,
                0.2
        );
        int hpBefore = monster.getHp();

        int dmg = DamageCalculator.heroPhysicalDamage(hero, monster);
        assert dmg >= 0 : "Damage should not be negative";
        int hpAfter = monster.getHp();

        assert hpBefore == hpAfter: "DamageCalculator shouldn't mutate monster";
        monster.takeDamage(dmg);
        int hpAfterDamage = monster.getHp();

        assert hpAfterDamage == hpBefore - dmg: "Monster HP not reducing correctly after applying damage";
    }

    public static void testUnarmedHeroDamage(){
        Hero hero = new Warrior("Bilbo Baggins", 50, 0, 10, 10, 0, 0);
        Monster monster = new Dragon("Smaug", 1, 30, 10, 0.0);

        int dmg = DamageCalculator.heroPhysicalDamage(hero, monster);
        assert dmg >= 0 : "Unarmed hero damage should be non-negative";
    }

    public static void testMonsterDamage() {
        Hero hero = new Warrior("Bilbo Baggins", 50, 20, 10, 10, 0, 0);
        Monster monster = new Dragon("Smaug", 1, 30, 10, 0.0);

        int hpBefore = hero.getHp();

        int dmg = DamageCalculator.monsterDamage(monster, hero);
        assert dmg >= 0 : "Monster damage must be non-negative";

        hero.takeDamage(dmg);
        assert hero.getHp() == hpBefore - dmg
                : "Hero HP not reduced correctly by monster damage";
    }
}
