package state;
import character.*;
import combat.HeroActionManager;
import world.GameMap;

import java.util.ArrayList;
import java.util.List;

public class GameContext {
    public List<Hero> heroes = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public GameMap map;
    public HeroActionManager actionManager;

    public boolean gameRunning = true;

    public boolean monsterReachedHeroNexus() {
        // returning false for now
        // TODO: implement actual logic
        return false;
    }

    public boolean heroReachedEnemyNexus() {
        // TODO: implement logic later
        return false;
    }
}
