package ai;
import character.Hero;
import character.Monster;
import combat.ValorCombatExecutor;
import combat.ValorCombatRules;
import world.Position;
import java.util.List;

public class MonsterAI {
    public void takeTurn(Monster monster, List<Hero> heroes){
        for (Hero hero: heroes){
            if (!hero.isAlive()){
                continue;
            }
            if (ValorCombatRules.canAttack(monster, hero)){
                ValorCombatExecutor.monsterAttack(monster, hero);
                return;
            }
        }
        moveForward(monster);
    }

    private void moveForward(Monster monster) {
        Position initialPos = monster.getPosition();
        if (initialPos == null){
            return;
        }
        monster.setPosition(
                new Position(
                        initialPos.row + 1,
                        initialPos.col,
                        initialPos.lane
                )
        );
    }
}
