package state;

public class GameOverState implements GameState {
    private final GameContext context;
    private boolean shown = false;

    public GameOverState(GameContext context) {
        this.context = context;
    }

    @Override
    public void enter() {

    }

    @Override
    public void update() {
        if (!shown){
            System.out.println("====== Game over ======");
            if (context.heroReachedEnemyNexus()){
                System.out.println("You win! Your heroes reached the enemy nexus");
            } else if (context.monsterReachedHeroNexus()){
                System.out.println("Defeat! The monsters reached your nexus");
            } else {
                System.out.println("Game ended");
            }
            System.out.println("========================");
            shown = true;
        }
        context.gameRunning = false;
    }

    @Override
    public void exit() {

    }
}
