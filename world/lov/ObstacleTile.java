package world.lov;

import world.mh.Tile;

public class ObstacleTile extends Tile {

    public ObstacleTile() {
        this.accessible = false;
    }

    @Override
    public char getSymbol() {
        return 'O';
    }
}