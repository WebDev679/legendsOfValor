package world.lov;

import java.util.Scanner;

import character.Hero;
import combat.action.QuitAction;
import world.lov.action.*;

public class ExplorationActionManager {
    private final Scanner scanner;
    private LoVBoard board;

    public ExplorationActionManager(Scanner scanner,  LoVBoard lovBoard) {
        this.board = lovBoard;
        this.scanner = scanner;
    }

    public ExplorationAction nextAction(LoVBoard board, int heroIndex){
        for (Hero hero : board.getHeroes()){
            while (true) {
                System.out.println("Action: W (up), S (down), A (left), D (right), T (teleport), R (recall), Q (quit)");
                String actionInput = scanner.nextLine().trim().toLowerCase();

                if (actionInput.isEmpty()){
                    return null;
                }

                if (actionInput.equals("q")) {
                    return new QuitGameAction();
                }

                char action = actionInput.charAt(0);

                switch (action){
                    case 'w':
                        return new MoveAction(heroIndex, Direction.UP);
                    case 's':
                        return new MoveAction(heroIndex, Direction.DOWN);
                    case 'd':
                        return new MoveAction(heroIndex, Direction.RIGHT);
                    case 'a':
                        return new MoveAction(heroIndex, Direction.LEFT);
                    case 'r':
                        return new RecallAction(heroIndex);
                    case 't':
                        return promptTeleport(heroIndex, board);
                    default:
                        System.out.println("Invalid action!");
                }
            }
        }
        return null;
    }

    private ExplorationAction promptTeleport(int heroIndex, LoVBoard board){
        try{
            System.out.println("Teleport to hero: ");
            int to =  Integer.parseInt(scanner.nextLine().trim()) -1;
            System.out.println("Row offset: (-1 or 1): ");
            int rowOffset = Integer.parseInt(scanner.nextLine().trim());
            return new TeleportAction(heroIndex, to, rowOffset);
        } catch (Exception e){
            return null;
        }
    }
}
