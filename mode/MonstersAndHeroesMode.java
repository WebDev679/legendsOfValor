package mode;


import engine.GameEngine;

public class MonstersAndHeroesMode implements GameMode {
    private final GameEngine engine;

    public MonstersAndHeroesMode() {
        this.engine = new GameEngine();
    }
    @Override
    public void start() {
        engine.start();
    }
}
