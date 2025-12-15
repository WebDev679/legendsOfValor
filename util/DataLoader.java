package util;// util.DataLoader.java
import character.*;
import item.*;

import java.io.*;
import java.util.*;

public class DataLoader {

    public static List<Warrior> loadWarriors() {
        List<Warrior> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Warriors.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 7) continue;
                String name = parts[0];
                int mana = Integer.parseInt(parts[1]);
                int strength = Integer.parseInt(parts[2]);
                int agility = Integer.parseInt(parts[3]);
                int dexterity = Integer.parseInt(parts[4]);
                int money = Integer.parseInt(parts[5]);
                int exp = Integer.parseInt(parts[6]);
                list.add(new Warrior(name, mana, strength, agility, dexterity, money, exp));
            }
        } catch (IOException e) {
            System.err.println("Could not load Warriors.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Paladin> loadPaladins() {
        List<Paladin> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Paladins.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 7) continue;
                String name = parts[0];
                int mana = Integer.parseInt(parts[1]);
                int strength = Integer.parseInt(parts[2]);
                int agility = Integer.parseInt(parts[3]);
                int dexterity = Integer.parseInt(parts[4]);
                int money = Integer.parseInt(parts[5]);
                int exp = Integer.parseInt(parts[6]);
                list.add(new Paladin(name, mana, strength, agility, dexterity, money, exp));
            }
        } catch (IOException e) {
            System.err.println("Could not load Paladins.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Sorcerer> loadSorcerers() {
        List<Sorcerer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Sorcerers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 7) continue;
                String name = parts[0];
                int mana = Integer.parseInt(parts[1]);
                int strength = Integer.parseInt(parts[2]);
                int agility = Integer.parseInt(parts[3]);
                int dexterity = Integer.parseInt(parts[4]);
                int money = Integer.parseInt(parts[5]);
                int exp = Integer.parseInt(parts[6]);
                list.add(new Sorcerer(name, mana, strength, agility, dexterity, money, exp));
            }
        } catch (IOException e) {
            System.err.println("Could not load Sorcerers.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Weapon> loadWeapons() {
        List<Weapon> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Weaponry.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int hands = Integer.parseInt(parts[4]);
                list.add(new Weapon(name, cost, level, damage, hands));
            }
        } catch (IOException e) {
            System.err.println("Could not load Weaponry.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Armor> loadArmors() {
        List<Armor> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Armory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 4) continue;
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int reduction = Integer.parseInt(parts[3]);
                list.add(new Armor(name, cost, level, reduction));
            }
        } catch (IOException e) {
            System.err.println("Could not load Armory.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Potion> loadPotions() {
        List<Potion> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Potions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int inc = Integer.parseInt(parts[3]);

                // join remaining tokens as attribute string
                StringBuilder attrBuilder = new StringBuilder();
                for (int i = 4; i < parts.length; i++) {
                    if (i > 4) attrBuilder.append(" ");
                    attrBuilder.append(parts[i]);
                }
                String attrStr = attrBuilder.toString().replace("All", "").trim();

                EnumSet<AttributeType> attrs = EnumSet.noneOf(AttributeType.class);
                String[] attrTokens = attrStr.split("[/\\s]+");
                for (String tok : attrTokens) {
                    if (tok.isEmpty()) continue;
                    switch (tok.toLowerCase()) {
                        case "health":
                            attrs.add(AttributeType.HEALTH); break;
                        case "mana":
                            attrs.add(AttributeType.MANA); break;
                        case "strength":
                            attrs.add(AttributeType.STRENGTH); break;
                        case "dexterity":
                            attrs.add(AttributeType.DEXTERITY); break;
                        case "defense":
                            attrs.add(AttributeType.DEFENSE); break;
                        case "agility":
                            attrs.add(AttributeType.AGILITY); break;
                    }
                }

                list.add(new Potion(name, cost, level, inc, attrs));
            }
        } catch (IOException e) {
            System.err.println("Could not load Potions.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Spell> loadFireSpells() {
        return loadSpells("resources/FireSpells.txt", SpellType.FIRE);
    }

    public static List<Spell> loadIceSpells() {
        return loadSpells("resources/IceSpells.txt", SpellType.ICE);
    }

    public static List<Spell> loadLightningSpells() {
        return loadSpells("resources/LightningSpells.txt", SpellType.LIGHTNING);
    }

    private static List<Spell> loadSpells(String filename, SpellType type) {
        List<Spell> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int mana = Integer.parseInt(parts[4]);
                list.add(new Spell(name, cost, level, damage, mana, type));
            }
        } catch (IOException e) {
            System.err.println("Could not load " + filename + ": " + e.getMessage());
        }
        return list;
    }

    public static List<Monster> generateRandomMonsters(int count, int maxHeroLevel) {
        List<Monster> all = new ArrayList<>();
        all.addAll(loadDragons());
        all.addAll(loadExoskeletons());
        all.addAll(loadSpirits());

        List<Monster> usable = new ArrayList<>();
        for (Monster m : all) {
            if (m.getLevel() <= maxHeroLevel) usable.add(m);
        }
        if (usable.isEmpty()) usable = all;

        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Monster proto = usable.get(random.nextInt(usable.size()));
            monsters.add(copyMonster(proto));
        }
        return monsters;
    }

    private static Monster copyMonster(Monster m) {
        if (m instanceof Dragon) return new Dragon((Dragon) m);
        if (m instanceof Exoskeleton) return new Exoskeleton((Exoskeleton) m);
        if (m instanceof Spirit) return new Spirit((Spirit) m);
        // fallback (should not happen)
        return m;
    }

    public static List<Monster> loadDragons() {
        List<Monster> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Dragons.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                int damage = Integer.parseInt(parts[2]);
                int defense = Integer.parseInt(parts[3]);
                double dodgeChance = Integer.parseInt(parts[4]) * 0.01;
                list.add(new Dragon(name, level, damage, defense, dodgeChance));
            }
        } catch (IOException e) {
            System.err.println("Could not load Dragons.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Monster> loadExoskeletons() {
        List<Monster> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Exoskeletons.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                int damage = Integer.parseInt(parts[2]);
                int defense = Integer.parseInt(parts[3]);
                double dodgeChance = Integer.parseInt(parts[4]) * 0.01;
                list.add(new Exoskeleton(name, level, damage, defense, dodgeChance));
            }
        } catch (IOException e) {
            System.err.println("Could not load Exoskeletons.txt: " + e.getMessage());
        }
        return list;
    }

    public static List<Monster> loadSpirits() {
        List<Monster> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/Spirits.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("Name")) continue;
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;
                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                int damage = Integer.parseInt(parts[2]);
                int defense = Integer.parseInt(parts[3]);
                double dodgeChance = Integer.parseInt(parts[4]) * 0.01;
                list.add(new Spirit(name, level, damage, defense, dodgeChance));
            }
        } catch (IOException e) {
            System.err.println("Could not load Spirits.txt: " + e.getMessage());
        }
        return list;
    }
}
