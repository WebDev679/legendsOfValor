package combat;
import character.Hero;
import character.Monster;
import ai.MonsterAI;
import java.util.List;

public class ValorBattle {
    private final MonsterAI monsterAI = new MonsterAI();

    public boolean execute(List<Hero> heroes, List <Monster> monsters) {
        while (hasAliveHero(heroes) && hasAliveMonster(monsters)){
            for (Hero hero: heroes){
                if (!hero.isAlive()){
                    continue;
                }

                Monster target = findMonsterInRange(hero, monsters);
                if (target != null){
                    ValorCombatExecutor.heroAttack(hero, target);
                }
            }
            for  (Monster monster: monsters){
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

    private Monster findMonsterInRange(Hero h, List<Monster> monsters) {
        for (Monster monster: monsters){
            if (!monster.isAlive()){
                continue;
            }
            if (ValorCombatRules.canAttack(h, monster)){
                return monster;
            }
        }
        return null;
    }
}
