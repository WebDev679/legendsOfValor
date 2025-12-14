package combat;
import character.Hero;
import character.Monster;
import ai.MonsterAI;
import java.util.List;
import combat.action.*;
import util.PrintUtils;

public class ValorBattle {
    private final MonsterAI monsterAI = new MonsterAI();

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
    }

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