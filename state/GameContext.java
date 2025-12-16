package state;

import character.*;
import combat.HeroActionManager;
import world.mh.GameMap;
import world.lov.LoVBoard;
import engine.MarketService;


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

    //  ADD THIS
    public LoVBoard lovBoard;

    //  ADD THIS
    public LoVBoard getLoVBoard() {
        return lovBoard;
    }

    public boolean monsterReachedHeroNexus() {
        return lovBoard != null && lovBoard.anyMonsterOnHeroNexus();
    }

    public boolean heroReachedEnemyNexus() {
        return lovBoard != null && lovBoard.anyHeroOnMonsterNexus();
    }

    public boolean hasAliveHeroes() {
        for (Hero hero : heroes) {
            if (hero.isAlive()) return true;
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
        return lovBoard != null && lovBoard.isHeroOnHeroNexus(hero);
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
