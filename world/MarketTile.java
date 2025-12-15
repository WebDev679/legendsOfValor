package world;

public class MarketTile extends Tile {
    public MarketTile() {
        this.accessible = true;
    }

    @Override
    public char getSymbol() {
        return 'M';
    }
}
