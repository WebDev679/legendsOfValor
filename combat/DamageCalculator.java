package combat;
import character.Hero;
import character.Monster;
import item.Armor;
import item.Spell;
import item.Weapon;

/**
 * Utility class for computing combat damage values
 * <p>This class centralises all damage formulae for heroes and monsters.
 * This ensures consistent behaviour across game. All methods are stateless</p>
 *
 * <p>Damage calculation take into account character stats, equipped items and defence
 * attributes using % based metric</p>
 */
public final class DamageCalculator {
    /** Prevent instantiation of utility class */
    private DamageCalculator(){}

    /**
     * Computes physical attack damage by hero to a monster
     * <p>Formula takes into acccount</p>
     * <ol>
     *     <li>Hero strength (weighted)</li>
     *     <li>Equipped weapon's damage</li>
     *     <li>Monster defence</li>
     * </ol>
     * @param hero attackign hero
     * @param target target monster
     * @return final physical damage (min 1)
     */
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

    /**
     * Computes spell damage bya hero to a monster
     * @param hero spellcasting hero
     * @param spell spell being cast
     * @param target target monster
     * @return final spell damage (min 1)
     */
    public static int heroSpellDamage(Hero hero, Spell spell, Monster target){
        double dexFactor = 1.0 + hero.getDexterity() * 0.01;
        double base = spell.getDamage() * dexFactor;
        double factor = 100.0 / (100.0 + target.getDefense());
        return Math.max(1, (int)(base * factor));
    }

    /**
     * COmputes damage bya monster to hero
     * <p>Damage reduced based on hero's equipped armor, following same heuristic
     * as hero attacks</p>
     * @param monster attacking the hero
     * @param target hero being attacked
     * @return final monster damage (min 1)
     */
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
