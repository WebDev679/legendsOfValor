package ai;

import character.Hero;
import character.Monster;
import combat.ValorCombatExecutor;
import combat.ValorCombatRules;

import java.util.List;

/**
 * Monster AI for Legends of Valor combat.
 * Handles combat decisions ONLY.
 */
public class MonsterAI {

    public void takeTurn(Monster monster, List<Hero> heroes) {

        // Try to attack any valid hero
        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;

            if (ValorCombatRules.canAttack(monster, hero)) {
                ValorCombatExecutor.monsterAttack(monster, hero);
                return;
            }
        }

        // If no attack possible → do nothing
        // (movement handled by LoVBoard / ExplorationState)
    }
}