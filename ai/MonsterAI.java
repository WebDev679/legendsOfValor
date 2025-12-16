package ai;

import character.Hero;
import character.Monster;
import combat.ValorCombatExecutor;
import combat.ValorCombatRules;
import world.lov.LoVBoard;
import world.lov.MonsterMove;

import java.util.List;

/**
 * Controls autonomous behaviour for monsters during their turn.
 *
 * Decision priority:
 * 1. Attack if a valid hero is in range
 * 2. Otherwise, request forward movement toward heroes' nexus
 *
 * This class only decides intent. It does NOT mutate board state.
 */
public class MonsterAI {

    /**
     * Attempts to perform an attack.
     *
     * @return true if an attack was executed, false otherwise
     */
    public boolean tryAttack(Monster monster, List<Hero> heroes) {
        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;

            if (ValorCombatRules.canAttack(monster, hero)) {
                ValorCombatExecutor.monsterAttack(monster, hero);
                return true;
            }
        }
        return false;
    }

    /**
     * Decide where the monster wants to move.
     * No validation is performed here.
     *
     * @param monster the monster
     * @param row     current row
     * @param col     current column
     * @return desired movement, or null if no movement
     */
    public MonsterMove decideMove(
            Monster monster,
            int row,
            int col,
            int lane,
            boolean forwardBlocked
    ) {
        if (!monster.isAlive()) return null;

        if (!forwardBlocked) {
            return new MonsterMove(row + 1, col);
        }

        // Forward blocked → try lateral within lane
        int[] laneCols = LoVBoard.columnsInLane(lane);

        for (int c : laneCols) {
            if (c != col) {
                return new MonsterMove(row, c);
            }
        }

        return null;
    }
}