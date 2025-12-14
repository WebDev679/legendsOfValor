package state;

import combat.QuitBattleException;
import combat.ValorBattle;
public class BattleState implements GameState {
    private final GameContext context;
    private final ValorBattle battle = new ValorBattle();
    private final StateManager stateManager;

    public BattleState(GameContext context, StateManager stateManager) {
        this.context = context;
        this.stateManager = stateManager;
    }


    @Override
    public void enter() {
        System.out.println("Entered battle phase");
    }

    @Override
    public void update() {
        try {
            battle.resolveRound(
                    context.heroes,
                    context.monsters,
                    context.actionManager
            );

        } catch (RuntimeException e) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        } catch (QuitBattleException e) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
        }

        if (context.heroReachedEnemyNexus()) {
            System.out.println("Heroes win!");
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        }

        if (context.monsterReachedHeroNexus()) {
            System.out.println("Monsters win!");
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        }

        if (context.hasAliveHeroes() && context.hasAliveMonsters()){
            return;
        }
        context.monsters.clear();
        stateManager.changeState(
                new ExplorationState(context, stateManager)
        );
    }

    @Override
    public void exit() {

    }
}
