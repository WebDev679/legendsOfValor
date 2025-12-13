package combat;
import character.Hero;
import character.Monster;
import ai.MonsterAI;
import java.util.List;
import combat.action.*;

public class ValorBattle {
    private final MonsterAI monsterAI = new MonsterAI();

    public boolean execute(
            List<Hero> heroes,
            List<Monster> monsters,
            HeroActionManager actionManager
    ) {

        while (hasAliveHero(heroes) && hasAliveMonster(monsters)) {

            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                HeroAction action = actionManager.nextAction(hero);
                resolveHeroAction(hero, action, monsters);
            }

            for (Monster monster : monsters) {
                if (!monster.isAlive()){
                    continue;
                }
                monsterAI.takeTurn(monster, heroes);
            }

            for (Hero hero: heroes){
                hero.regenAfterRound();
            }
        }
        return hasAliveHero(heroes);
    }

    private void resolveHeroAction(
            Hero hero,
            HeroAction action,
            List<Monster> monsters
    ) {

        if (action instanceof AttackAction) {
            AttackAction attack = (AttackAction) action;
            Monster target = attack.getTarget();

            if (!target.isAlive()) return;
            if (!ValorCombatRules.canAttack(hero, target)) return;

            ValorCombatExecutor.heroAttack(hero, target);
        }
    }

    private boolean hasAliveMonster(List<Monster> monsters) {
        for (Monster monster: monsters){
            if (monster.isAlive()){
                return true;
            }
        }
        return false;
    }

    private boolean hasAliveHero(List<Hero> heroes) {
        for (Hero hero: heroes){
            if (hero.isAlive()){
                return true;
            }
        }
        return false;
    }
}