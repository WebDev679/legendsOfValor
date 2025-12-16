package combat.test;

import character.Dragon;
import character.Hero;
import character.Monster;
import character.Warrior;
import combat.DamageCalculator;
import item.Armor;
import item.Weapon;

/**
 * Tests that equipping weapons and armor changes combat damage as expected.
 *
 * <p>Relies on Java assertions. Run with {@code -ea} to enable them.</p>
 */
public class EquipmentImpactTest {

    public static void main(String[] args) {
        run();
        System.out.println("✓ EquipmentImpactTest passed");
    }

    public static void run() {
        testWeaponIncreasesHeroDamage();
        testArmorReducesMonsterDamage();
    }

    private static void testWeaponIncreasesHeroDamage() {
        Hero hero = new Warrior("Tester", 100, 20, 10, 10, 0, 0);
        Monster monster = new Dragon("Dummy", 1, 20, 10, 0.0);

        int damageWithoutWeapon = DamageCalculator.heroPhysicalDamage(hero, monster);

        Weapon sword = new Weapon("Sword", 0, 1, 30, 1);
        hero.getInventory().addWeapon(sword);
        hero.getInventory().equipWeapon(0);

        int damageWithWeapon = DamageCalculator.heroPhysicalDamage(hero, monster);

        assert damageWithWeapon > damageWithoutWeapon
                : "Equipping a weapon should increase hero physical damage";
    }

    private static void testArmorReducesMonsterDamage() {
        Hero hero = new Warrior("Tester", 100, 20, 10, 10, 0, 0);
        Monster monster = new Dragon("Dummy", 1, 30, 10, 0.0);

        int damageWithoutArmor = DamageCalculator.monsterDamage(monster, hero);

        Armor armor = new Armor("Shield", 0, 1, 20);
        hero.getInventory().addArmor(armor);
        hero.getInventory().equipArmor(0);

        int damageWithArmor = DamageCalculator.monsterDamage(monster, hero);

        assert damageWithArmor < damageWithoutArmor
                : "Equipping armor should reduce damage taken from monsters";
    }
}


