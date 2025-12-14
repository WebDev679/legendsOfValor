package state;

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
            stateManager.changeState(null);
            return;
        }

        if (context.heroReachedEnemyNexus()) {
            System.out.println("Heroes win!");
            context.gameRunning = false;
            stateManager.changeState(null);
            return;
        }

        if (context.monsterReachedHeroNexus()) {
            System.out.println("Monsters win!");
            context.gameRunning = false;
            stateManager.changeState(null);
            return;
        }

        stateManager.changeState(
                new ExplorationState(context, stateManager)
        );
    }

    @Override
    public void exit() {

    }
}
