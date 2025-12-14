package world.lov;

import character.Hero;
import world.mh.Tile;

public class KoulouTile extends Tile {

    private static final int BONUS = 10;

    public KoulouTile() {
        this.accessible = true;
    }

    @Override
    public char getSymbol() {
        return 'K';
    }

    @Override
    public void onEnter(Hero hero) {
        hero.setStrength(hero.getStrength() + BONUS);
    }

    @Override
    public void onExit(Hero hero) {
        hero.setStrength(hero.getStrength() - BONUS);
    }
}