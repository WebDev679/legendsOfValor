package engine;

import character.Hero;
import item.Armor;
import item.Inventory;
import item.Weapon;

import java.util.List;
import java.util.Scanner;

/**
 * Shared text-based UI helper for changing hero equipment outside of battle.
 *
 * <p>This menu logic is reused by the legacy Monsters &amp; Heroes world
 * exploration and can also be invoked from Legends of Valor exploration
 * states by passing in the current hero list from {@link state.GameContext}.</p>
 */
public final class EquipmentMenu {

    private EquipmentMenu() {}

    /**
     * Allows the player to choose a hero and change their equipped weapon
     * or armor.
     *
     * @param heroes  list of heroes in the current party
     * @param scanner scanner used for user input
     */
    public static void changeEquipment(List<Hero> heroes, Scanner scanner) {
        if (heroes == null || heroes.isEmpty()) {
            System.out.println("No heroes available.");
            return;
        }

        System.out.println("\n== Change Equipment ==");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            System.out.printf("[%d] %s%n", i, h.getName());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose hero: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx < 0 || idx >= heroes.size()) {
            System.out.println("Invalid hero index.");
            return;
        }

        Hero hero = heroes.get(idx);
        Inventory inv = hero.getInventory();
        System.out.println("Change equipment for " + hero.getName());
        System.out.println("1. Equip weapon");
        System.out.println("2. Equip armor");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                List<Weapon> weapons = inv.getWeapons();
                if (weapons.isEmpty()) {
                    System.out.println("No weapons available.");
                    return;
                }
                System.out.println("Weapons:");
                for (int i = 0; i < weapons.size(); i++) {
                    Weapon w = weapons.get(i);
                    System.out.printf("[%d] %s (Dmg:%d, Hands:%d)%n",
                            i, w.getName(), w.getDamage(), w.getHandsRequired());
                }
                System.out.print("Choose weapon index: ");
                String inW = scanner.nextLine().trim();
                try {
                    int idxW = Integer.parseInt(inW);
                    if (idxW < 0 || idxW >= weapons.size()) {
                        System.out.println("Invalid index.");
                        return;
                    }
                    inv.equipWeapon(idxW);
                    System.out.println("Equipped " + weapons.get(idxW).getName());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
                break;

            case "2":
                List<Armor> armors = inv.getArmors();
                if (armors.isEmpty()) {
                    System.out.println("No armors available.");
                    return;
                }
                System.out.println("Armors:");
                for (int i = 0; i < armors.size(); i++) {
                    Armor a = armors.get(i);
                    System.out.printf("[%d] %s (Red:%d)%n",
                            i, a.getName(), a.getDamageReduction());
                }
                System.out.print("Choose armor index: ");
                String inA = scanner.nextLine().trim();
                try {
                    int idxA = Integer.parseInt(inA);
                    if (idxA < 0 || idxA >= armors.size()) {
                        System.out.println("Invalid index.");
                        return;
                    }
                    inv.equipArmor(idxA);
                    System.out.println("Equipped " + armors.get(idxA).getName());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
                break;

            case "3":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}


