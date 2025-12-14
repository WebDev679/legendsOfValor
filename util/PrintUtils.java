package util;

public class PrintUtils {
    private PrintUtils() {}

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
