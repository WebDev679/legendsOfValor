package world.mh;

import character.Hero;

public abstract class Tile {

    protected boolean accessible = true;

    public boolean isAccessible() {
        return accessible;
    }

    public abstract char getSymbol();

    // Default no-op lifecycle hooks
    public void onEnter(Hero hero) {}
    public void onExit(Hero hero) {}
}