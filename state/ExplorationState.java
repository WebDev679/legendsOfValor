package state;

import character.Hero;
import factory.MonsterFactory;
import util.DataLoader;

import java.util.Random;

public class ExplorationState implements GameState {
    private final GameContext context;
    private final StateManager stateManager;
    private final Random random = new Random();

    public ExplorationState(GameContext context, StateManager stateManager) {
        this.context = context;
        this.stateManager = stateManager;
    }

    @Override
    public void enter(){
        System.out.println("Starting exploration!");
    }

    @Override
    public void update() {
        if (random.nextDouble() < 0.2){
            System.out.println("Encountered a monster!");
            int heroesCount = context.heroes.size();
            int maxLevel = 1;
            for (Hero hero : context.heroes){
                maxLevel = Math.max(maxLevel, hero.getLevel());
            }

            context.monsters = MonsterFactory.spawnWave(heroesCount, maxLevel);
            stateManager.changeState(new BattleState(context, stateManager));

        }
    }

    @Override
    public void exit() {
    }
}
