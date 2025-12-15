package state;
import character.*;
import combat.HeroActionManager;
import engine.MarketService;
import world.GameMap;

import java.util.ArrayList;
import java.util.List;

public class GameContext {
    public List<Hero> heroes = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public GameMap map;
    public HeroActionManager actionManager;
    public MarketService marketService;

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

    /**
     * Returns true if the given hero is currently standing on a heroes' Nexus tile.
     *
     * <p>The exact logic depends on the Legends of Valor board implementation
     * and lane layout, which is handled elsewhere. This method is a hook that
     * can be implemented once the map and Nexus tiles are in place.</p>
     */
    public boolean isHeroOnNexus(Hero hero) {
        // TODO: implement actual Nexus position checks once map logic is in place.
        return false;
    }

    /**
     * Returns true if any hero is currently standing on a heroes' Nexus tile.
     */
    public boolean anyHeroOnNexus() {
        for (Hero hero : heroes) {
            if (isHeroOnNexus(hero)) {
                return true;
            }
        }
        return false;
    }
}
