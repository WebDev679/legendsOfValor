package world.lov.renderer;

import world.lov.LoVBoard;

public class AsciiBoardRenderer {

    private final LoVBoard board;

    // Toggle this if you ever want to disable colors
    private static final boolean USE_COLOR = true;

    /* ===== ANSI COLOR CODES ===== */
    private static final String RESET   = "\u001B[0m";
    private static final String RED     = "\u001B[31m";
    private static final String GREEN   = "\u001B[32m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN    = "\u001B[36m";
    private static final String WHITE   = "\u001B[37m";

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
                System.out.print(" " + colorize(ch) + " |");
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

    /* ===== COLOR HELPER ===== */
    private String colorize(char ch) {
        if (!USE_COLOR) return String.valueOf(ch);

        switch (ch) {
            case 'H': // Hero
                return GREEN + ch + RESET;
            case 'M': // Monster
                return RED + ch + RESET;
            case 'N': // Nexus
                return CYAN + ch + RESET;
            case 'X': // Inaccessible wall
                return WHITE + ch + RESET;
            case 'O': // Obstacle
                return YELLOW + ch + RESET;
            case 'B': // Bush
            case 'K': // Cave / special tile
                return MAGENTA + ch + RESET;
            default:  // Common tile, etc.
                return String.valueOf(ch);
        }
    }
}
