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

        GameContext context = new GameContext();

        /*
         * TEMPORARY:
         * Heroes must already exist in context.heroes
         * (loaded by partner / DataLoader / factory)
         */
        List<Hero> heroes = context.heroes;

        if (heroes == null || heroes.isEmpty()) {
            System.out.println("No heroes loaded. Exiting LoV mode.");
            return;
        }

        // ✅ Difficulty is selected HERE
        context.lovBoard = new LoVBoard(
                heroes,
                LoVBoard.Difficulty.MEDIUM
        );

        StateManager sm = new StateManager(context);
        sm.changeState(new ExplorationState(context, sm, context.lovBoard));

        while (context.gameRunning) {
            sm.update();
        }
    }
}
