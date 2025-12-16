package world.lov;

import world.mh.CommonTile;
import world.mh.Tile;

import java.util.Random;

public class LoVTileFactory {

    private static final Random rand = new Random();

    public static Tile createPlayableTile() {
        double r = rand.nextDouble();

        if (r < 0.40) return new CommonTile();
        if (r < 0.55) return new BushTile();
        if (r < 0.70) return new CaveTile();
        if (r < 0.85) return new KoulouTile();
        return new ObstacleTile();
    }
}