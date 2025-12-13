package state;
import character.*;
import world.GameMap;
import java.util.List;

public class GameContext {
    public List<Hero> heroes;
    public List<Monster> monsters;
    public GameMap map;

    public boolean gameRunning = true;
}
