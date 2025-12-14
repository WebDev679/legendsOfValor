package world.lov;

import world.mh.Tile;
import java.util.List;

public interface Board {
    void render();
    Tile getTile(int row, int col);
    boolean isWithinBounds(int row, int col);

    int getHeroCount();
    int getHeroRow(int index);
    int getHeroCol(int index);

    List<MonsterPosition> getMonsters();
}