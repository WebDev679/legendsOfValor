package combat;
import character.Hero;
import character.Monster;
import item.Spell;

/**
 * Executes combat actions between heroes and monsters
 *
 * <p>This contains final logic for combat, including damage applicaiton, dodge check,
 * mana consumption and printing outcomes to console</p>
 *
 * <p>Intentionally doesn't decide <i>when</i> an action occures.
 * That is handled by {@link ValorBattle} and {@link HeroActionManager}. This keeps
 * combat resolutoin free from turn management and input handling</p>
 */
public final class ValorCombatExecutor {
    private ValorCombatExecutor(){}

    /**
     * Executes attack by monster on hero
     * <p>Attack may miss if a hero dodges successfully.
     * If attack hits then damage is calculated via {@link DamageCalculator#monsterDamage(Monster, Hero)}
     * and applied directly to hero</p>
     *
     * @param monster the attacking monster
     * @param hero hero being attacked
     */
    public static void monsterAttack(Monster monster, Hero hero){
        if (Math.random() < hero.getDodgeChance()){
            System.out.println(hero.getName() + "dodged attack from " + monster.getName());
            return;
        }

        int damage = DamageCalculator.monsterDamage(monster, hero);

        hero.takeDamage(damage);

        System.out.printf(
                "%s hits %s for %d damage.%n",
                monster.getName(), hero.getName(), damage
        );
    }

    /**
     * Execcutes hero physical attack against a monster
     * <p>Attack may fail if monster dodges successfully. On successful
     * aattack, damage calculated in {@link DamageCalculator#heroPhysicalDamage(Hero, Monster)} and
     * appplied to monster</p>
     * @param hero attacking hero
     * @param monster target monster
     */
    public static void heroAttack(Hero hero, Monster monster){
        if (Math.random() < monster.getDodgeChance()){
            System.out.println(monster.getName() + " dodged attack from " +  hero.getName());
            return;
        }

        int damage = DamageCalculator.heroPhysicalDamage(hero, monster);
        monster.takeDamage(damage);

        System.out.printf(
                "%s hits %s for %d damage.%n",
                hero.getName(), monster.getName(), damage
        );
    }

    /**
     * Executes hero spell against monster
     * <p>Hero must have enough mana to cast spell.
     * If monster dodges then no damage or other effects are paplied.
     * On successful hit damage is calculated using
     * {@link DamageCalculator#heroSpellDamage(Hero, Spell, Monster)} and special effect is triggered</p>
     * @param hero casting the spell
     * @param monster target monster
     * @param spell spell being cast
     */
    public static void heroCastSpell(Hero hero, Monster monster, Spell spell){
        if (!hero.spendMana(spell.getManaCost())){
            System.out.println(hero.getName() + " does not have enough mana");
            return;
        }

        if (Math.random() < monster.getDodgeChance()){
            System.out.println(monster.getName() + " dodged the spell from " + hero.getName());
            return;
        }

        int damage = DamageCalculator.heroSpellDamage(hero, spell, monster);
        monster.takeDamage(damage);
        spell.applyEffect(monster);

        System.out.printf(
                "%s casts %s on %s for %d damage.%n",
                hero.getName(), spell.getName(), monster.getName(), damage
        );
    }

}
