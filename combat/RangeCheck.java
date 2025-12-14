package combat;
import world.mh.Position;

public class RangeCheck {
    private RangeCheck(){}

    public static boolean inRange(Position pos, Position other){
        if (pos == null || other == null){
            return false;
        }

        int deltaRow = Math.abs(pos.row - other.row);
        int deltaCol = Math.abs(pos.col - other.col);

        return (deltaRow + deltaCol) <= 1;
    }
}
