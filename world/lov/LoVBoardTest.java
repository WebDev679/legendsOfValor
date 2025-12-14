package world.lov;

import character.Hero;

import java.util.ArrayList;
import java.util.List;

public class LoVBoardTest {

    public static void main(String[] args) {

        List<Hero> heroes = new ArrayList<>();
        heroes.add(null);
        heroes.add(null);
        heroes.add(null);

        LoVBoard board = new LoVBoard(heroes);

        System.out.println("=== Legends of Valor Board Test ===");
        board.render();
    }
}