package combat;
import character.Hero;
import character.Monster;
import ai.MonsterAI;
import java.util.List;
import combat.action.*;
import item.Potion;
import util.PrintUtils;

/**
 * Manages the resolution of one combat round between heroes and monsters
 * <p>A round consists of</p>
 * <ol>
 *     <li>Print current battle state</li>
 *     <li>Heroes taking exactly one action each</li>
 *     <li>Monsters execute ai driven turns</li>
 *     <li>End of round regen for all living heroes</li>
 * </ol>
 *
 * <p>This class doesn't manage game state transition or start/end conditions.
 * It must be repeatedly involked by a higher level GameSate controller</p>
 */

public class ValorBattle {
    /** Monster AI behaviour controller*/
    private final MonsterAI monsterAI = new MonsterAI();

    /**
     * Resolve one combat round
     * <p>Each hero selects one action through {@link HeroActionManager}.
     * Actions get immediately resolved. Post all heroes acting, each
     * alive monster gets a turn</p>
     * @param heroes heroes list participating in battle
     * @param monsters list of monsters participating in battle
     * @param actionManager provider for hero actions (can be player driven or automated)
     * @throws QuitBattleException If quit action issued, then we throw this error
     */
    public void resolveRound(
            List<Hero> heroes,
            List<Monster> monsters,
            HeroActionManager actionManager
    ) throws QuitBattleException {

        PrintUtils.pause(500);
//        PrintUtils.clearScreen();
//
//        System.out.println("\n========= ROUND =========");
        printBattleState(heroes, monsters);

        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;

            HeroAction action = actionManager.nextAction(hero);
            resolveHeroAction(action, monsters);
        }

        System.out.println("\n======== Monsters' Turn ========");
        for (Monster monster : monsters) {
            if (!monster.isAlive()) continue;
            monsterAI.takeTurn(monster, heroes);
        }

        System.out.println("\n======== End of Round Regen ========");
        for (Hero hero : heroes) {
            hero.regenAfterRound();
        }
    }

    /**
     * Resolves single hero action
     * <p>Supports attack, spellcasting, potion use,
     * turn skip and quitting battle.
     * Null actions are ignored safely</p>
     * @param action action to resolve
     * @param monsters lisat of monsters in combat
     * @throws QuitBattleException thrown if action a quit response is indicated
     */
    private void resolveHeroAction(
            HeroAction action,
            List<Monster> monsters
    ) throws QuitBattleException {

        if (action == null) return;

        if (action instanceof QuitAction) {
            throw new QuitBattleException();
        }

        if (action instanceof SkipAction) {
            System.out.println(
                    ((SkipAction) action).getHero().getName() + " skipped their turn."
            );
            return;
        }

        if (action instanceof AttackAction) {
            AttackAction attack = (AttackAction) action;
            Hero attacker = attack.getAttacker();
            Monster target = attack.getTarget();

            if (!target.isAlive()) return;
            if (!ValorCombatRules.canAttack(attacker, target)) return;

            ValorCombatExecutor.heroAttack(attacker, target);
            return;
        }

        if (action instanceof SpellcastAction){
            SpellcastAction spellcast = (SpellcastAction) action;
            Hero caster = spellcast.getHero();
            Monster target = spellcast.getTarget();

            if (!target.isAlive()) return;
            if (!ValorCombatRules.canAttack(caster, target)) return;

            ValorCombatExecutor.heroCastSpell(
                    caster,
                    target,
                    spellcast.getSpell()
            );
        }

        if (action instanceof PotionAction){
            PotionAction potionAction = (PotionAction) action;
            Hero hero = potionAction.getHero();
            Potion potion = potionAction.getPotion();
            potion.applyTo(hero);
            hero.getInventory().removePotion(potion);

            System.out.println(hero.getName() + " used potion " + potion.getName());
            return;
        }
    }

    /**
     * Helper for printing current health and mana status of all heroes
     * and monsters
     * @param heroes list of heroes in battle
     * @param monsters list of monsters in battle
     */
    private void printBattleState(List<Hero> heroes, List<Monster> monsters) {
        System.out.println("\nHeroes:");
        for (Hero hero : heroes) {
            System.out.printf(
                    " %s | HP %d/%d | MP %d/%d%n",
                    hero.getName(),
                    hero.getHp(), hero.getMaxHp(),
                    hero.getMp(), hero.getMaxMp()
            );
        }

        System.out.println("\nMonsters:");
        for (Monster monster : monsters) {
            System.out.printf(
                    " %s | HP %d/%d%n",
                    monster.getName(),
                    monster.getHp(),
                    monster.getMaxHp()
            );
        }
    }
}