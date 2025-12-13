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
        System.out.println("Entered battle!");
        boolean won = battle.execute(context.heroes, context.monsters, context.actionManager);
        if (!won){
            context.gameRunning = false;
            stateManager.changeState(null);
            return;
        }
        context.monsters.clear();
        stateManager.changeState(new ExplorationState(context, stateManager));
    }

    @Override
    public void update() {

    }

    @Override
    public void exit() {

    }
}
