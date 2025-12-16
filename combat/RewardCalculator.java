package combat;

import character.Monster;

public final class RewardCalculator {
    public static int xpFor(Monster m) {
        return m.getLevel() * 20;
    }

    public static int goldFor(Monster m) {
        return m.getLevel() * 10;
    }
}
