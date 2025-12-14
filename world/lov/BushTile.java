package world.lov;

import character.Hero;
import world.mh.Tile;

public class BushTile extends Tile {

    private static final int BONUS = 10;

    public BushTile() {
        this.accessible = true;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }

    @Override
    public void onEnter(Hero hero) {
        hero.setDexterity(hero.getDexterity() + BONUS);
    }

    @Override
    public void onExit(Hero hero) {
        hero.setDexterity(hero.getDexterity() - BONUS);
    }
}