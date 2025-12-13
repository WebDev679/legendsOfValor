package state;

public class ExplorationState implements GameState {
    private final GameContext context;

    public ExplorationState(GameContext context) {
        this.context = context;
    }

    @Override
    public void enter(){
        System.out.println("Starting exploration!");
    }

    @Override
    public void update() {
    }

    @Override
    public void exit() {
    }
}
