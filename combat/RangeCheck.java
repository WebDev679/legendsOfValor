package combat;
import world.Position;

/**
 * Responsible for determining whether 2 positions are close enough to have combat
 * <p>Current implementation works with manhattan distance and considers range to be smae space or
 * immediately adjacent space</p>
 */
public class RangeCheck {
    /** Private constructor as this is a utility class */
    private RangeCheck(){}

    /**
     * Determines whether 2 positoins are in combat range
     * <p>In range if manhattan distance is <= 1. This allows attacks in same space
     * or directly acjacent space</p>
     * <p>Returns false if either position is null</p>
     * @param pos  Position of attacker
     * @param other posiotoin of defender
     * @return true if within range, false otherwise
     */
    public static boolean inRange(Position pos, Position other){
        if (pos == null || other == null){
            return false;
        }

        int deltaRow = Math.abs(pos.row - other.row);
        int deltaCol = Math.abs(pos.col - other.col);

        return (deltaRow + deltaCol) <= 1;
    }
}
