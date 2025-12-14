package state;

import combat.HeroActionManager;
import combat.PlayerActionManager;
import combat.QuitBattleException;
import combat.ValorBattle;
import world.lov.LoVBoard;

import java.util.Scanner;

public class BattleState implements GameState {

    private final GameContext context;
    private final StateManager stateManager;
    private final LoVBoard board;

    private final ValorBattle battle = new ValorBattle();
    private HeroActionManager previousActionManager;

    public BattleState(GameContext context, StateManager stateManager, LoVBoard board) {
        this.context = context;
        this.stateManager = stateManager;
        this.board = board;
    }

    @Override
    public void enter() {
        System.out.println("Entered Battle State");

        previousActionManager = context.actionManager;
        context.actionManager =
                new PlayerActionManager(new Scanner(System.in), context.monsters);
    }

    @Override
    public void update() {
        try {
            battle.resolveRound(
                    context.heroes,
                    context.monsters,
                    context.actionManager
            );
        } catch (QuitBattleException e) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        }

        // ================= POST-BATTLE CHECK =================

        // If monster is defeated, remove it from board
        if (!context.hasAliveMonsters()) {

            board.removeMonsterAtLastCollision();

            context.monsters.clear();

            // Resume exploration
            stateManager.changeState(
                    new ExplorationState(context, stateManager, board)
            );
            return;
        }

        // If heroes are defeated
        if (!context.hasAliveHeroes()) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
        }
    }

    @Override
    public void exit() {
        context.actionManager = previousActionManager;
        System.out.println("Exiting Battle State");
    }
}