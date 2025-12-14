package character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Party {
    private final List<Hero> heroes;

    public Party(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public boolean hasAliveHero() {
        for (Hero h : heroes) {
            if (h.isAlive()) return true;
        }
        return false;
    }

    public int getMaxLevel() {
        int max = 1;
        for (Hero h : heroes) {
            if (h.getLevel() > max) max = h.getLevel();
        }
        return max;
    }

    public Hero getRandomAliveHero(Random random) {
        List<Hero> alive = new ArrayList<>();
        for (Hero h : heroes) {
            if (h.isAlive()) alive.add(h);
        }
        if (alive.isEmpty()) return null;
        return alive.get(random.nextInt(alive.size()));
    }

    public void printSummary() {
        for (Hero h : heroes) {
            System.out.printf("%s (%s) L:%d HP:%d/%d MP:%d/%d Gold:%d%n",
                    h.getName(),
                    h.getClass().getSimpleName(),
                    h.getLevel(),
                    h.getHp(), h.getMaxHp(),
                    h.getMp(), h.getMaxMp(),
                    h.getGold());
        }
    }
}
