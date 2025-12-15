package combat;
import character.Hero;
import character.Monster;

/**
 * Utility class containing stat progression and combat related formulae. Cenralised
 * locaiotn for levelling, regen and dodge logic to have consistent balance across
 * characters
 */
public final class StatCalculator {
    private StatCalculator() {}

    public static int expToNextLevel(Hero h) {
        return h.getLevel() * 10;
    }

    public static int newMaxHpAfterLevelUp(Hero h) {
        return h.getLevel() * 100;
    }

    public static int newMaxMpAfterLevelUp(Hero h) {
        return (int) (h.getMaxMp() * 1.1);
    }

    public static int newStatAfterLevelUp(int prev) {
        return (int) (prev * 1.05);
    }

    public static double dodgeChanceFromAgility(int agility) {
        return agility * 0.002;
    }

    public static int regen10Percent(int current) {
        return current + (int) (current * 0.1);
    }

    public static double monsterDodgeChance(double base) {
        return Math.min(0.5, Math.max(0.0, base));
    }
}
