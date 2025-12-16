package mode;

import character.Hero;
import state.*;
import world.lov.LoVBoard;

import java.util.List;
import engine.GameEngine;
import state.ExplorationState;
import state.GameContext;
import state.StateManager;

public class LegendsOfValorMode implements GameMode {

    @Override
    public void start() {
        GameContext gameContext = new GameContext();
        StateManager stateManager = new StateManager(gameContext);

        /*
         * TEMPORARY:
         * Heroes must already exist in context.heroes
         * (loaded by partner / DataLoader / factory)
         */
        List<Hero> heroes = gameContext.heroes;

        if (heroes == null || heroes.isEmpty()) {
            System.out.println("No heroes loaded. Exiting LoV mode.");
            return;
        }

        // ✅ Difficulty is selected HERE
        gameContext.lovBoard = new LoVBoard(
                heroes,
                LoVBoard.Difficulty.MEDIUM
        );

        stateManager.changeState(new ExplorationState(gameContext, stateManager, gameContext.lovBoard));

        while (gameContext.gameRunning) {
            stateManager.update();
        }
    }
}
