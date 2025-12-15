package world;// world.GameMap.java
import java.util.Random;

public class GameMap {

    private final int size;
    private final Tile[][] grid;
    private int heroRow;
    private int heroCol;

    public GameMap(int size) {
        this.size = size;
        this.grid = new Tile[size][size];
        generateRandomMap();
    }

    private void generateRandomMap() {
        Random random = new Random();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                double roll = random.nextDouble();
                if (roll < 0.2) {
                    grid[r][c] = new InaccessibleTile();
                } else if (roll < 0.5) {
                    grid[r][c] = new MarketTile();
                } else {
                    grid[r][c] = new CommonTile();
                }
            }
        }

        // Ensure starting cell is accessible and common
        grid[0][0] = new CommonTile();
        heroRow = 0;
        heroCol = 0;
    }

    public void printMap() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (r == heroRow && c == heroCol) {
                    System.out.print("H  ");
                } else {
                    System.out.print(grid[r][c].getSymbol() + "  ");
                }
            }
            System.out.println();
        }
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public Tile getTile(int row, int col) {
        return grid[row][col];
    }

    public void setHeroPosition(int row, int col) {
        heroRow = row;
        heroCol = col;
    }

    public int getHeroRow() {
        return heroRow;
    }

    public int getHeroCol() {
        return heroCol;
    }
}

