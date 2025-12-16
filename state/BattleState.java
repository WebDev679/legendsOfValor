package state;

import character.Hero;
import character.Monster;
import combat.HeroActionManager;
import combat.PlayerActionManager;
import combat.QuitBattleException;
import combat.ValorBattle;
import world.lov.LoVBoard;

import java.util.List;
import java.util.Scanner;

public class BattleState implements GameState {

    private final GameContext context;
    private final StateManager stateManager;
    private final LoVBoard board;

    private final ValorBattle battle = new ValorBattle();
    private HeroActionManager previousActionManager;
    private final List<Hero> heroes;
    private final List<Monster> monsters;

    public BattleState(GameContext context, StateManager stateManager, LoVBoard board,  List<Hero> heroes, List<Monster> monsters) {
        this.context = context;
        this.stateManager = stateManager;
        this.board = board;
        this.heroes = heroes;
        this.monsters = monsters;
    }


    @Override
    public void enter() {
        System.out.println("Entered battle phase");
        board.render();
        previousActionManager = context.actionManager;
        context.actionManager =
                new PlayerActionManager(new Scanner(System.in), this.monsters);
    }

    @Override
    public void update() {
        try {
            ValorBattle.BattleResult result = battle.resolveRound(
                    heroes,
                    monsters,
                    context.actionManager
            );
            if (result == ValorBattle.BattleResult.HERO_VICTORY){
                StringBuilder heroesStr = new StringBuilder();
                for (Hero hero : heroes) {
                    heroesStr.append(hero.getName()).append(", ");
                }
                System.out.println("Heroes " + heroesStr.toString() + " won battle!");
                distributeRewards(heroes, monsters);
            }
            context.round++;

        } catch (RuntimeException e) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        } catch (QuitBattleException e) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
        }

        // ================= POST-BATTLE CHECK =================

        // If monster is defeated, remove it from board
        if (!context.hasAliveMonsters()) {

//            board.removeMonsterAtLastCollision();

            context.monsters.clear();

            // Resume exploration
            stateManager.changeState(
                    new ExplorationState(context, stateManager, board)
            );
            if (context.heroReachedEnemyNexus()) {
                System.out.println("Heroes win!");
                context.gameRunning = false;
                stateManager.changeState(new GameOverState(context));
                return;
            }

            if (context.monsterReachedHeroNexus()) {
                System.out.println("Monsters win!");
                context.gameRunning = false;
                stateManager.changeState(new GameOverState(context));
                return;
            }

            if (context.hasAliveHeroes() && context.hasAliveMonsters()) {
                return;
            }
            context.monsters.clear();
            stateManager.changeState(
                    new ExplorationState(context, stateManager, board)
            );
        }
    }

    public final class RewardCalculator {
        public static int xpFor(Monster m) {
            return m.getLevel() * 20;
        }

        public static int goldFor(Monster m) {
            return m.getLevel() * 10;
        }
    }

    private void distributeRewards(List<Hero> heroes, List<Monster> monsters) {
        int totalXp = monsters.stream().filter(
                monster -> !monster.isAlive())
                .mapToInt(RewardCalculator::xpFor).sum();


        int totalGold = monsters.stream().filter(
                        monster -> !monster.isAlive())
                .mapToInt(RewardCalculator::goldFor).sum();

        int aliveHeroes = (int) heroes.stream().filter(Hero::isAlive).count();
        if (aliveHeroes == 0) return;

        int xpEach = totalXp / aliveHeroes;
        int goldEach = totalGold / aliveHeroes;

        for (Hero hero : heroes) {
            if (!hero.isAlive()) continue;
            hero.gainExperience(xpEach);
            hero.addGold(goldEach);
            System.out.println(
                    hero.getName() + " gained " + xpEach + " XP and " + goldEach + " gold."
            );
        }
    }
    @Override
    public void exit() {
        context.actionManager = previousActionManager;
        System.out.println("Exiting Battle State");
    }
}
