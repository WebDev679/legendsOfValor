package combat;
import character.Hero;
import character.Monster;
import item.Armor;
import item.Spell;
import item.Weapon;

public final class DamageCalculator {
    private DamageCalculator(){}

    public static int heroPhysicalDamage(Hero hero, Monster target){
        int weaponDamage = 0;
        Weapon weapon = hero.getEquippedWeapon();

        if (weapon != null){
            weaponDamage = weapon.getDamage();
        }

        double base = (hero.getStrength()* 1.5 + weaponDamage);
        double factor = 100.0 / (100.0 + target.getDefense());
        return Math.max(1, (int)(base * factor));
    }

    public static int heroSpellDamage(Hero hero, Spell spell, Monster target){
        double dexFactor = 1.0 + hero.getDexterity() * 0.01;
        double base = spell.getDamage() * dexFactor;
        double factor = 100.0 / (100.0 + target.getDefense());
        return Math.max(1, (int)(base * factor));
    }

    public static int monsterDamage(Monster monster, Hero target){
        int armorRed = 0;
        Armor armor = target.getEquippedArmor();
        if (armor != null){
            armorRed = armor.getDamageReduction();
        }
        double base = monster.getDamage() * 1.2;
        double factor = 100.0 / (100.0 + armorRed);
        return Math.max(1, (int)(base * factor));
    }
}
