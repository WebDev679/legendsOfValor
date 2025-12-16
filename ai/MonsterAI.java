package ai;

import character.Hero;
import character.Monster;
import combat.ValorCombatExecutor;
import combat.ValorCombatRules;
import java.util.List;


/**
 * Controls autonomous behaviour for monsters during their turn
 * <p>The MonsterAI follows a priority based model: </p>
 * <ul>
 *     <li>If an eligible hero is in range, attack</li>
 *     <li>Otherwise, move forward toward nexus</li>
 * </ul>
 * <p>This class encapsulates all monster decision making logic
 * and aloowes combat flow to be independent of AI strategy details</p>
 */
public class MonsterAI {
    /**
     * Executes a single turn for given monster
     * <p>The monster will attempt to attack first valid alive hero
     * within range. If none exist, monster advances forward by a space.</p>
     *
     * @param monster the monster taking its turn
     * @param heroes  the list of heroes currently ingame
     */

    public void takeTurn(Monster monster, List<Hero> heroes) {

        // Try to attack any valid hero
        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;

            if (ValorCombatRules.canAttack(monster, hero)) {
                ValorCombatExecutor.monsterAttack(monster, hero);
                return;
            }
        }
//        moveForward(monster);
    }
}

//    /**
//     * Moves monster forward one space towards heroes side
//     * <p>This method purely handles movement. Validation and lane boundaries are
//     * handled elsehwer in game logic</p>
//     * @param monster the monster to move
//     */
//    private void moveForward(Monster monster) {
//        Position initialPos = monster.getPosition();
//        if (initialPos == null){
//            return;
//        }
//        monster.setPosition(
//                new Position(
//                        initialPos.row + 1,
//                        initialPos.col,
//                        initialPos.lane
//                )
//        );
//    }
//}
