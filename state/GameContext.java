package state;

import character.*;
import combat.HeroActionManager;
import world.mh.GameMap;
import world.lov.LoVBoard;

import java.util.ArrayList;
import java.util.List;

public class GameContext {
    public List<Hero> heroes = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public GameMap map;
    public HeroActionManager actionManager;

    public int round = 1;
    public boolean gameRunning = true;

    //  ADD THIS
    public LoVBoard lovBoard;

    //  ADD THIS
    public LoVBoard getLoVBoard() {
        return lovBoard;
    }

    public boolean monsterReachedHeroNexus() {
        // If any LoV monster reaches bottom row, LoVBoard will return MONSTER_WIN.
        // Keep this for later refinement.
        return false;
    }

    public boolean heroReachedEnemyNexus() {
        // If any LoV hero reaches top row, LoVBoard will return HERO_WIN.
        // Keep this for later refinement.
        return false;
    }

    public boolean hasAliveHeroes() {
        for (Hero hero : heroes) {
            if (hero.isAlive()) return true;
        }
        return false;
    }

    public boolean hasAliveMonsters() {
        for (Monster monster : monsters) {
            if (monster.isAlive()) return true;
        }
        return false;
    }
}