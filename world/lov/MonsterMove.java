package world.lov;

import character.Monster;
import factory.MonsterFactory;

public class MonsterMove {
    public final int newRow;
    public final int newColumn;

    public MonsterMove(int newRow, int newColumn) {
        this.newRow = newRow;
        this.newColumn = newColumn;
    }
}
