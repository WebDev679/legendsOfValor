package state;

import world.lov.*;
import java.util.Scanner;

public class ExplorationState implements GameState {

    private final GameContext context;
    private final StateManager stateManager;
    private final LoVBoard board;

    private final Scanner scanner = new Scanner(System.in);

    public ExplorationState(GameContext context, StateManager stateManager, LoVBoard board) {
        this.context = context;
        this.stateManager = stateManager;
        this.board = board;
    }

    @Override
    public void enter() {
        System.out.println("Entered Exploration State");
    }

    @Override
    public void update() {

        board.render();

        System.out.print("Select hero (1-" + board.getHeroCount() + ", Q to quit): ");
        String heroInput = scanner.nextLine().trim().toUpperCase();

        if (heroInput.equals("Q")) {
            context.gameRunning = false;
            return;
        }

        int heroIndex;
        try {
            heroIndex = Integer.parseInt(heroInput) - 1;
        } catch (NumberFormatException e) {
            return;
        }

        if (heroIndex < 0 || heroIndex >= board.getHeroCount()) {
            return;
        }

        System.out.print("Action: W (up), S (down), T (teleport), R (recall): ");
        String actionInput = scanner.nextLine().trim().toUpperCase();

        if (actionInput.isEmpty()) return;

        char action = actionInput.charAt(0);

        /* ================= MOVE ================= */
        if (action == 'W' || action == 'S') {

            Direction dir = (action == 'W') ? Direction.UP : Direction.DOWN;

            WorldEvent heroEvent = board.moveHero(heroIndex, dir);
            if (handleWorldEvent(heroEvent)) return;

            WorldEvent monsterEvent = board.moveMonstersAI();
            handleWorldEvent(monsterEvent);
            return;
        }

        /* ================= TELEPORT ================= */
        if (action == 'T') {

            System.out.print("Teleport to which hero (1-" + board.getHeroCount() + "): ");
            String targetInput = scanner.nextLine().trim();

            int targetHero;
            try {
                targetHero = Integer.parseInt(targetInput) - 1;
            } catch (NumberFormatException e) {
                return;
            }

            if (targetHero < 0 || targetHero >= board.getHeroCount()) return;

            System.out.print("Row offset (-1 for above, 1 for below): ");
            String offsetInput = scanner.nextLine().trim();

            int offset;
            try {
                offset = Integer.parseInt(offsetInput);
            } catch (NumberFormatException e) {
                return;
            }

            LoVBoard.TeleportResult result =
                    board.teleportHero(heroIndex, targetHero, offset);

            if (result == LoVBoard.TeleportResult.INVALID) {
                System.out.println("Teleport failed.");
            }

            WorldEvent monsterEvent = board.moveMonstersAI();
            handleWorldEvent(monsterEvent);
            return;
        }

        /* ================= RECALL ================= */
        if (action == 'R') {

            LoVBoard.RecallResult result = board.recallHero(heroIndex);

            if (result == LoVBoard.RecallResult.INVALID) {
                System.out.println("Recall failed.");
            }

            WorldEvent monsterEvent = board.moveMonstersAI();
            handleWorldEvent(monsterEvent);
        }
    }

    private boolean handleWorldEvent(WorldEvent event) {

        if (event == WorldEvent.BATTLE_TRIGGERED) {
            stateManager.changeState(
                    new BattleState(context, stateManager, board)
            );
            return true;
        }

        if (event == WorldEvent.HERO_WIN || event == WorldEvent.MONSTER_WIN) {
            stateManager.changeState(new GameOverState(context));
            context.gameRunning = false;
            return true;
        }

        return false;
    }

    @Override
    public void exit() {
        System.out.println("Exiting Exploration State");
    }
}