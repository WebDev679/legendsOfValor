package state;

import ai.MonsterAI;
import character.Hero;
import character.Monster;
import world.lov.*;
import world.lov.action.*;

import java.util.List;
import java.util.Scanner;

public class ExplorationState implements GameState {

    private final GameContext context;
    private final StateManager stateManager;
    private final LoVBoard board;
    private final ExplorationActionManager actionManager;

    private final MonsterAI monsterAI = new MonsterAI();
    private int heroesActedThisRound= 0;

    public ExplorationState(GameContext context, StateManager stateManager, LoVBoard board) {
        this.context = context;
        this.stateManager = stateManager;
        this.board = board;
        this.actionManager = new ExplorationActionManager(new Scanner(System.in), this.board);
    }

    @Override
    public void enter() {
        System.out.println("Entered Exploration State");
    }

    @Override
    public void update() {
        board.render();

        ExplorationAction action = actionManager.nextAction(board);
        if (action == null) return;

        if (action instanceof QuitGameAction) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
            return;
        }

        WorldEvent heroEvent = WorldEvent.NONE;
        boolean actionSucceeded = true;

        if (action instanceof MoveAction) {
            MoveAction move = (MoveAction) action;
            heroEvent = board.moveHero(move.heroIndex, move.direction);
        }

        else if (action instanceof TeleportAction) {
            TeleportAction t = (TeleportAction) action;
            LoVBoard.TeleportResult r =
                    board.teleportHero(t.fromHero, t.toHero, t.rowOffset);

            if (r == LoVBoard.TeleportResult.INVALID) {
                System.out.println("Invalid teleport.");
                actionSucceeded = false;
            }
        }

        else if (action instanceof RecallAction) {
            RecallAction recall = (RecallAction) action;
            LoVBoard.RecallResult r = board.recallHero(recall.heroIndex);

            if (r == LoVBoard.RecallResult.INVALID) {
                System.out.println("Invalid recall.");
                actionSucceeded = false;
            }
        }

        handleWorldEvent(heroEvent);

        if (action instanceof RecallAction || action instanceof TeleportAction || action instanceof MoveAction) {
            heroesActedThisRound++;
        }

        if (heroesActedThisRound == board.getHeroCount()){
            WorldEvent monsterEvent = board.moveMonstersAI(monsterAI);
            handleWorldEvent(monsterEvent);
            heroesActedThisRound = 0;
        }

    }

    private void handleWorldEvent(WorldEvent event) {

        if (event == WorldEvent.BATTLE_TRIGGERED) {
            List<Hero> battleHeroes = board.getHeroesInBattleLane();
            List<Monster> battleMonsters = board.getMonstersInBattleLane();

            stateManager.changeState(
                    new BattleState(
                            context,
                            stateManager,
                            board,
                            battleHeroes,
                            battleMonsters
                    )
            );

//            context.monsters.clear();
//            context.monsters.addAll(board.getMonstersInBattleLane());

//            stateManager.changeState(
//                    new BattleState(context, stateManager, board)
//            );





        }

        if (event == WorldEvent.HERO_WIN || event == WorldEvent.MONSTER_WIN) {
            context.gameRunning = false;
            stateManager.changeState(new GameOverState(context));
        }
    }

    @Override
    public void exit() {
        System.out.println("Exiting Exploration State");
    }
}