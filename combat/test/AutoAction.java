package combat.test;

import character.Hero;
import character.Monster;
import combat.HeroActionManager;
import combat.action.*;
import item.Potion;
import item.Spell;

import java.util.List;

public class AutoAction implements HeroActionManager {

    private final List<Monster> monsters;

    public AutoAction(List<Monster> monsters) {
        this.monsters = monsters;
    }

    @Override
    public HeroAction nextAction(Hero hero) {

        if (!hero.isAlive()) {
            return new SkipAction(hero);
        }

        //  Emergency potion logic (below 30% HP)
        if (hero.getHp() < hero.getMaxHp() * 0.3) {
            Potion potion = firstPotion(hero);
            if (potion != null) {
                System.out.println(hero.getName() + " auto-uses potion " + potion.getName());
                return new PotionAction(hero, potion);
            }
        }

        // picking weakest monster
        Monster target = weakestMonster();
        if (target == null) {
            return new SkipAction(hero);
        }

        //Try to cast strongest usable spell
        Spell bestSpell = strongestUsableSpell(hero);
        if (bestSpell != null) {
            System.out.println(
                    hero.getName() + " auto-casts " + bestSpell.getName() +
                            " on " + target.getName()
            );
            return new SpellcastAction(hero, bestSpell, target);
        }

        // basic attack fallback
        System.out.println(
                hero.getName() + " auto-attacks " + target.getName()
        );
        return new AttackAction(hero, target);
    }


    private Monster weakestMonster() {
        Monster best = null;
        for (Monster m : monsters) {
            if (!m.isAlive()) continue;
            if (best == null || m.getHp() < best.getHp()) {
                best = m;
            }
        }
        return best;
    }

    private Spell strongestUsableSpell(Hero hero) {
        Spell best = null;
        for (Spell s : hero.getInventory().getSpells()) {
            if (hero.getMp() < s.getManaCost()) continue;
            if (best == null || s.getDamage() > best.getDamage()) {
                best = s;
            }
        }
        return best;
    }

    private Potion firstPotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) return null;
        return potions.get(0);
    }
}