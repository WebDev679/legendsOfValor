package factory;

import character.*;
import util.DataLoader;

import java.util.List;

public final class HeroFactory {

    private HeroFactory() {}

    public static Hero createHero(Hero prototype) {
        return prototype.createCopy();
    }

    public static List<Warrior> loadWarriors() {
        return DataLoader.loadWarriors();
    }

    public static List<Paladin> loadPaladins() {
        return DataLoader.loadPaladins();
    }

    public static List<Sorcerer> loadSorcerers() {
        return DataLoader.loadSorcerers();
    }
}