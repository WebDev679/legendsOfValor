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

    public int round = 1;
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

    public boolean hasAliveHeroes() {
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAliveMonsters() {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
