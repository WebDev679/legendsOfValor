package state;

/**
 * Represents high level game phase
 *
 * <p>Each Gamestate implementation encapsulates behaviour for a specific
 * phase of the game such as game over, battle, exploration, etc and controls
 * how the game reponds during that phase</p>
 */
public interface GameState {
    void enter();
    void update();
    void exit();
}
