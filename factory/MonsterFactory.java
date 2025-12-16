package factory;

import character.Monster;
import util.DataLoader;

import java.util.List;
import java.util.Random;

/**
 * Factory class for creating {@link Monster} instances for game.
 *
 * <p>Encapsulates monster creation by delegating loading and filtering to {@link DataLoader}. This ensures
 * that monsters are spawned based on current game context such as max hero level</p>
 *
 * <p>Factory supports spawning single monster or wave of monster, allowing combat and world to be
 * decoupled from monster creation and selectoin</p>
 *
 * <p>This follows the factory design pattern and is intentionally stateless</p>
 */
public final class MonsterFactory {

    /** Private constructor to prevent instantiation. This
     * class provides only static factory methods */
    private MonsterFactory() {}

    /**
     * Spawns wave of monsters tied to currenet game state
     * @param count number of monsters to spawn
     * @param maxHeroLevel max level among all heroes, used to scale monster difficulty
     * @return list of newly created {@link Monster} instances
     */
    public static List<Monster> spawnWave(int count, int maxHeroLevel) {
        return DataLoader.generateRandomMonsters(count, maxHeroLevel);
    }

    /**
     * Spwns single monster appropriate for currenet game state
     * @param maxHeroLevel max level among all heroes used to scale monster diffculty
     * @return newly created {@link Monster} instance
     */
    public static Monster spawnSingle(int maxHeroLevel) {
        List<Monster> list = DataLoader.generateRandomMonsters(1, maxHeroLevel);
        return list.get(0);
    }
}