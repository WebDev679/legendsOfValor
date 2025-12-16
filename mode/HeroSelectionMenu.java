package mode;

import character.*;
import factory.HeroFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HeroSelectionMenu {

    private final Scanner scanner;

    public HeroSelectionMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public List<Hero> chooseHeroes(int count) {
        List<Hero> chosen = new ArrayList<>();

        List<Hero> allPrototypes = new ArrayList<>();
        allPrototypes.addAll(HeroFactory.loadWarriors());
        allPrototypes.addAll(HeroFactory.loadPaladins());
        allPrototypes.addAll(HeroFactory.loadSorcerers());

        System.out.println("=== Choose Your Heroes ===");

        while (chosen.size() < count) {
            printPrototypes(allPrototypes);

            System.out.print("Select hero " + (chosen.size() + 1) + ": ");
            String input = scanner.nextLine().trim();

            try {
                int idx = Integer.parseInt(input) - 1;
                if (idx < 0 || idx >= allPrototypes.size()) {
                    System.out.println("Invalid choice.");
                    continue;
                }

                Hero prototype = allPrototypes.get(idx);
                Hero hero = HeroFactory.createHero(prototype);
                chosen.add(hero);

                System.out.println("Added: " + hero.getName());

            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }

        return chosen;
    }

    private void printPrototypes(List<Hero> prototypes) {
        System.out.println("\nAvailable Heroes:");
        for (int i = 0; i < prototypes.size(); i++) {
            Hero h = prototypes.get(i);
            System.out.printf(
                    "%d. %s (%s, Lvl %d)%n",
                    i + 1,
                    h.getName(),
                    h.getClass().getSimpleName(),
                    h.getLevel()
            );
        }
    }
}