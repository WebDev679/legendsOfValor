package mode;

import engine.GameEngine;
import state.ExplorationState;
import state.GameContext;
import state.StateManager;

public class LegendsOfValorMode implements GameMode {

    @Override
    public void start() {
        GameContext gameContext = new GameContext();
        StateManager stateManager = new StateManager();

        stateManager.changeState(new ExplorationState(gameContext, stateManager));

        while (gameContext.gameRunning){
            stateManager.update();
        }
    }
}
