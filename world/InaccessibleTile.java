package world;

public class InaccessibleTile extends Tile {
    public InaccessibleTile() {
        this.accessible = false;
    }

    @Override
    public char getSymbol() {
        return 'X';
    }
}
