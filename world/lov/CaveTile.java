package world.lov;

import character.Hero;
import world.mh.Tile;

public class CaveTile extends Tile {

    private static final int BONUS = 10;

    public CaveTile() {
        this.accessible = true;
    }

    @Override
    public char getSymbol() {
        return 'C';
    }

    @Override
    public void onEnter(Hero hero) {
        hero.setAgility(hero.getAgility() + BONUS);
    }

    @Override
    public void onExit(Hero hero) {
        hero.setAgility(hero.getAgility() - BONUS);
    }
}