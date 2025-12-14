package world.mh;

public class CommonTile extends Tile {
    public CommonTile() {
        this.accessible = true;
    }

    @Override
    public char getSymbol() {
        return 'C';
    }
}
