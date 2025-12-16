package world.lov.action;

public class TeleportAction implements ExplorationAction{
    public final int fromHero;
    public final int toHero;
    public final int rowOffset;

    public TeleportAction(int fromHero, int toHero, int rowOffset) {
        this.fromHero = fromHero;
        this.toHero = toHero;
        this.rowOffset = rowOffset;
    }
}
