package factory;

import character.*;
import util.DataLoader;

import java.util.List;

/**
 * Factory class for creating and loading {@link Hero} instances.
 *
 * <p>
 *     This class centralises hero creation and separates it
 *     from gameplay and state code. It supports both:
 * </p>
 * <ul>
 *     <li>Loading new hero prototypes from external data</li>
 *     <li>Creating new hero instances by copying prototype</li>
 * </ul>
 *
 * <p>This class follows factory design pattern and also implements prototype pattern
 * to create nw heroes by copying existing prototypes</p>
 */
public final class HeroFactory {

    /** Private constructor since this class only gives static factory methods */
    private HeroFactory() {}

    /**
     * Uses prototype pattern to ensure each hero instance is independent and can be
     * modifed without affecting original prototype
     * @param prototype hero prototype to copy
     * @return a new {@link Hero} instance with identical initial attributes
     */
    public static Hero createHero(Hero prototype) {
        return prototype.createCopy();
    }

    /**
     * Loads all available {@link Warrior} hero prototypes from external dataset
     * @return list of warrior hero prototyps
     */
    public static List<Warrior> loadWarriors() {
        return DataLoader.loadWarriors();
    }

    /**
     * Loads all available {@link Paladin} hero prototypes from external dataset
     * @return list of paladin hero prototypes
     */
    public static List<Paladin> loadPaladins() {
        return DataLoader.loadPaladins();
    }

    /**
     * Loads all available {@link Sorcerer} prototypes from external dataset
     * @return list of sorcerer hero prototypes
     */
    public static List<Sorcerer> loadSorcerers() {
        return DataLoader.loadSorcerers();
    }
}