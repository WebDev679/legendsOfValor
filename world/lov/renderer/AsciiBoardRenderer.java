package world.lov.renderer;

import world.lov.LoVBoard;

public class AsciiBoardRenderer {

    private final LoVBoard board;

    public AsciiBoardRenderer(LoVBoard board) {
        this.board = board;
    }

    public void render() {
        // Each cell prints as " X " (3 chars). Borders make it aligned.
        printBorder();
        for (int r = 0; r < LoVBoard.SIZE; r++) {
            System.out.print("|");
            for (int c = 0; c < LoVBoard.SIZE; c++) {
                char ch = board.getOccupant(r, c);
                System.out.print(" " + ch + " |");
            }
            System.out.println();
            printBorder();
        }
    }

    private void printBorder() {
        for (int c = 0; c < LoVBoard.SIZE; c++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}