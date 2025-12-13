package state;

import combat.ValorBattle;
public class BattleState implements GameState {
    private final GameContext context;
    private final ValorBattle battle = new ValorBattle();

    public BattleState(GameContext context) {
        this.context = context;
    }


    @Override
    public void enter() {
        System.out.println("Entered battle!");
        boolean won = battle.execute(context.heroes, context.monsters);

        if (!won){
            context.gameRunning = false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void exit() {

    }
}
