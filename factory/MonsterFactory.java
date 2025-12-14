package factory;

import character.Monster;
import util.DataLoader;

import java.util.List;
import java.util.Random;

public final class MonsterFactory {

    private MonsterFactory() {}

    public static List<Monster> spawnWave(int count, int maxHeroLevel) {
        return DataLoader.generateRandomMonsters(count, maxHeroLevel);
    }

    public static Monster spawnSingle(int maxHeroLevel) {
        List<Monster> list = DataLoader.generateRandomMonsters(1, maxHeroLevel);
        return list.get(0);
    }
}