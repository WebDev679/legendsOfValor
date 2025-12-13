package combat.test;

import character.Dragon;
import character.Hero;
import character.Monster;
import character.Sorcerer;
import combat.DamageCalculator;
import item.Spell;
import item.SpellType;

public class SpellTest {

    public static void run(){
        testHeroSpellDamage();
        testFireSpellEffect();
        testIceSpellEffect();
        testLightningSpellEffect();
        System.out.println("✓ Spell tests passed");
    }
    public static void testHeroSpellDamage() {
        Hero hero = new Sorcerer("Gandalf", 100, 10, 10, 30, 0, 0);
        Monster monster = new Dragon("Smaug", 1, 30, 10, 0.0);

        Spell spell = new Spell("Fireball", 0, 1, 50, 30, SpellType.FIRE);
        int hpBefore =  monster.getHp();
        int mpBefore =  hero.getMp();

        int dmg = DamageCalculator.heroSpellDamage(hero, spell, monster);

        assert dmg > 0: "Spell damage must be > 0";
        assert monster.getHp() == hpBefore: "Damage calculation must not mutate monster HP";
        assert hero.getMp() == mpBefore: "Damage calculation must not mutate hero MP";

        System.out.println("SpellDamageTest passed");
    }

    public static void testFireSpellEffect(){
        Monster monster = new Dragon("Smaug", 1, 30, 10, 0.0);
        int defenceBefore = monster.getDefense();

        Spell fireball = new Spell("Fireball", 0, 1, 50, 30, SpellType.FIRE);
        fireball.applyEffect(monster);

        assert monster.getDefense() < defenceBefore: "Fire spell should reduce defense";
    }

    public static void testIceSpellEffect() {
        Monster monster = new Dragon("Smaug", 1, 30, 20, 0.5);

        int damageBefore = monster.getDamage();

        Spell ice = new Spell(
                "Frostbite",
                0, 1,
                40, 15,
                SpellType.ICE
        );

        ice.applyEffect(monster);

        assert monster.getDamage() < damageBefore
                : "Ice spell should reduce monster damage";
    }

    public static void testLightningSpellEffect() {
        Monster monster = new Dragon("Smaug", 1, 30, 20, 0.8);

        double dodgeBefore = monster.getDodgeChance();

        Spell lightning = new Spell(
                "Lightning Bolt",
                0, 1,
                45, 18,
                SpellType.LIGHTNING
        );

        lightning.applyEffect(monster);

        assert monster.getDodgeChance() < dodgeBefore
                : "Lightning spell should reduce dodge chance";
    }
}
