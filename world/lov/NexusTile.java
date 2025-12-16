package world.lov;

import world.mh.MarketTile;

public class NexusTile extends MarketTile {

    public enum Owner {
        HERO,
        MONSTER
    }

    private final Owner owner;

    public NexusTile(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public char getSymbol() {
        return 'N';
    }
}