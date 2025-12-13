package combat;
import character.Hero;
import character.Monster;
import combat.action.AttackAction;
import combat.action.HeroAction;
import combat.action.SpellcastAction;
import item.Spell;
import java.util.List;

public final class ValorCombatExecutor {
    private ValorCombatExecutor(){}

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

    public static void heroAttack(Hero hero, Monster monster){
        if (Math.random() < monster.getDodgeChance()){
            System.out.println(monster.getName() + " dodged attack from " +  hero.getName());
            return;
        }

        int damage = DamageCalculator.heroPhysicalDamage(hero, monster);

        System.out.printf(
                "%s hits %s for %d damage.%n",
                hero.getName(), monster.getName(), damage
        );
    }

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

    private void resolveAction(Hero hero, HeroAction action, List<Monster> monsters){
        if (action instanceof AttackAction){
            AttackAction attack = (AttackAction) action;
            Monster target = attack.getTarget();

            if (!target.isAlive()){
                return;
            }

            if (!ValorCombatRules.canAttack(hero, target)){
                return;
            }
            ValorCombatExecutor.heroAttack(hero, target);
        } else if (action instanceof SpellcastAction){
            SpellcastAction spellcast = (SpellcastAction) action;
            Monster target = spellcast.getTarget();

            if (!target.isAlive()){
                return;
            }

            if (!ValorCombatRules.canAttack(hero, target)){
                return;
            }

            ValorCombatExecutor.heroCastSpell(hero, target, spellcast.getSpell());

        }
    }

}
