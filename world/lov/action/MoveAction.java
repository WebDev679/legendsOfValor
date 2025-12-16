package world.lov.action;

import world.lov.Direction;

public class MoveAction implements ExplorationAction {
    public final int heroIndex;
    public final Direction direction;

    public MoveAction(int heroIndex, Direction direction) {
        this.heroIndex = heroIndex;
        this.direction = direction;
    }
}
