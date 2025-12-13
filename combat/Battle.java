package combat;// combat.Battle.java
import character.Hero;
import character.Monster;
import character.Party;
import item.*;
import combat.DamageCalculator;

import java.util.*;

public class Battle {

    private Random random = new Random();

    public boolean execute(Party party, List<Monster> monsters, Scanner scanner) {
        System.out.println("\n*** BATTLE START ***");
        printBattleStatus(party, monsters);

        while (party.hasAliveHero() && hasAliveMonster(monsters)) {
            heroPhase(party, monsters, scanner);
            if (!hasAliveMonster(monsters)) break;

            monsterPhase(party, monsters);
            endOfRoundRegen(party);

            printBattleStatus(party, monsters);
        }

        if (party.hasAliveHero()) {
            handleVictory(party, monsters);
            System.out.println("*** BATTLE WON ***");
            return true;
        } else {
            System.out.println("*** BATTLE LOST ***");
            return false;
        }
    }

    private boolean hasAliveMonster(List<Monster> monsters) {
        for (Monster m : monsters) {
            if (m.isAlive()) return true;
        }
        return false;
    }

    private void heroPhase(Party party, List<Monster> monsters, Scanner scanner) {
        for (Hero hero : party.getHeroes()) {
            if (!hero.isAlive()) {
                System.out.println(hero.getName() + " has fainted and cannot act.");
                continue;
            }

            boolean turnDone = false;
            while (!turnDone) {
                System.out.println("\nAction for " + hero.getName() + ":");
                System.out.println("1. Attack");
                System.out.println("2. Cast item.Spell");
                System.out.println("3. Use item.Potion");
                System.out.println("4. Change Equipment");
                System.out.println("5. Info (heroes & monsters)");
                System.out.println("6. Skip turn");
                System.out.print("Choose (or I for info, Q to quit): ");
            
                String choice = scanner.nextLine().trim();
            
                if (choice.equalsIgnoreCase("q")) {
                    System.out.println("Quitting game...");
                    System.exit(0);
                }
                if (choice.equalsIgnoreCase("i")) {
                    printBattleStatus(party, monsters);
                    continue; // does NOT consume turn
                }
            
                switch (choice) {
                    case "1":
                        heroAttack(hero, monsters, scanner);
                        turnDone = true;
                        break;
                    case "2":
                        castSpell(hero, monsters, scanner);
                        turnDone = true;
                        break;
                    case "3":
                        usePotion(hero, scanner);
                        turnDone = true;
                        break;
                    case "4":
                        changeEquipment(hero, scanner);
                        turnDone = true; // using equip as a turn
                        break;
                    case "5":
                        printBattleStatus(party, monsters);
                        break;
                    case "6":
                        turnDone = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
            
        }
    }

    private void heroAttack(Hero hero, List<Monster> monsters, Scanner scanner) {
        List<Monster> aliveMonsters = new ArrayList<>();
        for (Monster m : monsters) if (m.isAlive()) aliveMonsters.add(m);

        if (aliveMonsters.isEmpty()) {
            System.out.println("No monsters to attack.");
            return;
        }

        System.out.println("Choose target:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.isAlive()) {
                System.out.printf("[%d] %s (HP:%d/%d)%n", i, m.getName(), m.getHp(), m.getMaxHp());
            }
        }
        System.out.println("[X] Cancel");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (idx < 0 || idx >= monsters.size() || !monsters.get(idx).isAlive()) {
            System.out.println("Invalid target.");
            return;
        }

        Monster target = monsters.get(idx);

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println(target.getName() + " dodged the attack!");
            return;
        }

        int damage = DamageCalculator.heroPhysicalDamage(hero, target);
        target.takeDamage(damage);
        System.out.printf("%s hits %s for %d damage.%n", hero.getName(), target.getName(), damage);
    }

    private void castSpell(Hero hero, List<Monster> monsters, Scanner scanner) {
        List<Spell> spells = hero.getInventory().getSpells();
        if (spells.isEmpty()) {
            System.out.println("No spells available.");
            return;
        }

        System.out.println("Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("[%d] %s (%s Dmg:%d, MP:%d)%n",
                    i, s.getName(), s.getType(), s.getDamage(), s.getManaCost());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose spell index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int sIdx;
        try {
            sIdx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (sIdx < 0 || sIdx >= spells.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Spell spell = spells.get(sIdx);
        if (hero.getMp() < spell.getManaCost()) {
            System.out.println("Not enough mana.");
            return;
        }

        // Choose target
        System.out.println("Choose target:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.isAlive()) {
                System.out.printf("[%d] %s (HP:%d/%d)%n", i, m.getName(), m.getHp(), m.getMaxHp());
            }
        }
        System.out.println("[X] Cancel");
        in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }
        if (idx < 0 || idx >= monsters.size() || !monsters.get(idx).isAlive()) {
            System.out.println("Invalid target.");
            return;
        }
        Monster target = monsters.get(idx);

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println(target.getName() + " dodged the spell!");
            return;
        }

        // Cast
        hero.spendMana(spell.getManaCost());
        int dmg = DamageCalculator.heroSpellDamage(hero, spell, target);
        target.takeDamage(dmg);
        spell.applyEffect(target);
        System.out.printf("%s casts %s on %s for %d damage.%n",
                hero.getName(), spell.getName(), target.getName(), dmg);

        // Spells are consumable
        spells.remove(sIdx);
    }

    private void usePotion(Hero hero, Scanner scanner) {
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions available.");
            return;
        }
        System.out.println("Potions:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("[%d] %s (Increase:%d)%n",
                    i, p.getName(), p.getAttributeIncrease());
        }
        System.out.println("[X] Cancel");
        System.out.print("Choose potion index: ");
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("x")) return;

        int idx;
        try {
            idx = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (idx < 0 || idx >= potions.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Potion potion = potions.remove(idx);
        potion.applyTo(hero);
        System.out.println("Used potion " + potion.getName());
    }

    private void changeEquipment(Hero hero, Scanner scanner) {
        Inventory inv = hero.getInventory();
        System.out.println("\nChange equipment for " + hero.getName());
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

    private void monsterPhase(Party party, List<Monster> monsters) {
        System.out.println("\n--- Monsters' turn ---");
        for (Monster monster : monsters) {
            if (!monster.isAlive()) continue;

            Hero target = party.getRandomAliveHero(random);
            if (target == null) return;

            if (random.nextDouble() < target.getDodgeChance()) {
                System.out.println(target.getName() + " dodged attack from " + monster.getName());
                continue;
            }

            int dmg = DamageCalculator.monsterDamage(monster, target);
            target.takeDamage(dmg);
            System.out.printf("%s hits %s for %d damage.%n", monster.getName(), target.getName(), dmg);
        }
    }

    private void endOfRoundRegen(Party party) {
        for (Hero h : party.getHeroes()) {
            h.regenAfterRound();
        }
    }

    private void handleVictory(Party party, List<Monster> monsters) {
        int monstersCount = monsters.size();
    
        int expReward = monstersCount * 2;
    

        int monsterLevelForGold = monsters.isEmpty() ? 1 : monsters.get(0).getLevel();
        int goldReward = monsterLevelForGold * 100;
    
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) {
                h.addGold(goldReward);
                h.gainExperience(expReward);
            } else {
                h.reviveAfterBattle(); // revived with half HP/MP, no gold or XP
            }
        }
    
        System.out.printf(
            "Each surviving hero gained %d gold and %d XP.%n",
            goldReward, expReward
        );
    }
    

    private void printBattleStatus(Party party, List<Monster> monsters) {
        System.out.println("\n--- Heroes ---");
        for (Hero h : party.getHeroes()) {
            System.out.printf("%s (HP:%d/%d, MP:%d/%d)%n",
                    h.getName(), h.getHp(), h.getMaxHp(),
                    h.getMp(), h.getMaxMp());
        }

        System.out.println("--- Monsters ---");
        for (Monster m : monsters) {
            System.out.printf("%s (HP:%d/%d, Dmg:%d, Def:%d, Dodge:%.2f)%n",
                    m.getName(), m.getHp(), m.getMaxHp(),
                    m.getDamage(), m.getDefense(), m.getDodgeChance());
        }
    }
}
