package mode;

import character.Hero;
import state.*;
import world.lov.LoVBoard;
import java.util.*;

public class LegendsOfValorMode implements GameMode {

    @Override
    public void start() {
        GameContext gameContext = new GameContext();
        StateManager stateManager = new StateManager(gameContext);

        Scanner sc = new Scanner(System.in);
        HeroSelectionMenu menu = new HeroSelectionMenu(sc);
        gameContext.heroes = menu.chooseHeroes(3);
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
