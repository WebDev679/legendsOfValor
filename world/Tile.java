package world;

public abstract class Tile {
    protected boolean accessible;

    public boolean isAccessible() {
        return accessible;
    }

    public abstract char getSymbol();
}
