package state;

public class StateManager {
    private GameState currentState;

    public void changeState(GameState nextState) {
        if(currentState != null){
            currentState.exit();
        }

        currentState = nextState;
        currentState.enter();
    }

    public void update(){
        if(currentState != null){
            currentState.update();
        }
    }
}
