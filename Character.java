// Character.java
import java.util.*;

public abstract class Character {
    protected String name;
    protected int level;
    protected int hp;
    protected int maxHp;

    public Character(String name, int level) {
        this.name = name;
        this.level = level;
        this.maxHp = level * 100;
        this.hp = maxHp;
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }
}

// ---------------- HEROES ----------------

abstract class Hero extends Character {
    protected int mp;
    protected int maxMp;
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected int gold;
    protected int experience;
    protected Inventory inventory = new Inventory();

    protected Hero(String name, int level, int hp, int mp, int strength,
                   int dexterity, int agility, int gold, int experience) {
        super(name, level);
        this.maxHp = hp;
        this.hp = hp;
        this.maxMp = mp;
        this.mp = mp;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.experience = experience;
    }

    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getAgility() { return agility; }
    public int getGold() { return gold; }
    public int getExperience() { return experience; }

    public Inventory getInventory() { return inventory; }

    public Weapon getEquippedWeapon() { return inventory.getEquippedWeapon(); }
    public Armor getEquippedArmor() { return inventory.getEquippedArmor(); }

    public void addGold(int amount) {
        gold += amount;
        if (gold < 0) gold = 0;
    }

    public void spendGold(int amount) {
        gold -= amount;
        if (gold < 0) gold = 0;
    }

    public void gainExperience(int exp) {
        experience += exp;
        while (experience >= level * 10) {
            experience -= level * 10;
            levelUp();
        }
    }

    protected void baseLevelUp() {
        level++;
        maxHp = level * 100;
        hp = maxHp;

        maxMp = (int) (maxMp * 1.1);
        mp = maxMp;

        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);
    }

    protected abstract void applyClassBonusesOnLevelUp();

    public void levelUp() {
        baseLevelUp();
        applyClassBonusesOnLevelUp();
        System.out.println(name + " leveled up to level " + level + "!");
    }

    public double getDodgeChance() {
        return agility * 0.002;
    }

    public boolean spendMana(int cost) {
        if (mp < cost) return false;
        mp -= cost;
        return true;
    }

    public void regenAfterRound() {
        if (!isAlive()) return;
        hp += (int) (hp * 0.1);
        if (hp > maxHp) hp = maxHp;
        mp += (int) (mp * 0.1);
        if (mp > maxMp) mp = maxMp;
    }

    public void reviveAfterBattle() {
        if (!isAlive()) {
            hp = maxHp / 2;
            mp = maxMp / 2;
        }
    }

    public String detailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%s) - Lvl %d%n", name,
                getClass().getSimpleName(), level));
        sb.append(String.format("HP: %d/%d  MP: %d/%d%n", hp, maxHp, mp, maxMp));
        sb.append(String.format("STR: %d  DEX: %d  AGI: %d%n",
                strength, dexterity, agility));
        sb.append(String.format("Gold: %d  EXP: %d/%d%n",
                gold, experience, level * 10));
        sb.append("Equipped weapon: ");
        sb.append(getEquippedWeapon() != null ? getEquippedWeapon().getName() : "None");
        sb.append("\nEquipped armor: ");
        sb.append(getEquippedArmor() != null ? getEquippedArmor().getName() : "None");
        sb.append("\nInventory:\n");
        sb.append(inventory.describe());
        return sb.toString();
    }

    public abstract Hero createCopy();
}

class Warrior extends Hero {
    public Warrior(String name, int mana, int strength, int agility,
                   int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    // Copy constructor
    public Warrior(Warrior other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: strength & agility
        strength = (int) (strength * 1.05);
        agility = (int) (agility * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Warrior(this);
    }
}

class Paladin extends Hero {
    public Paladin(String name, int mana, int strength, int agility,
                   int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    public Paladin(Paladin other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: strength & dexterity
        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Paladin(this);
    }
}

class Sorcerer extends Hero {
    public Sorcerer(String name, int mana, int strength, int agility,
                    int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    public Sorcerer(Sorcerer other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: dexterity & agility
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Sorcerer(this);
    }
}

// ---------------- MONSTERS ----------------

abstract class Monster extends Character {
    protected int damage;
    protected int defense;
    protected double dodgeChance; // 0..1

    public Monster(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level);
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    public int getDamage() { return damage; }
    public int getDefense() { return defense; }
    public double getDodgeChance() { return dodgeChance; }

    public void reduceDamage(double factor) {
        damage = (int) (damage * factor);
        if (damage < 0) damage = 0;
    }

    public void reduceDefense(double factor) {
        defense = (int) (defense * factor);
        if (defense < 0) defense = 0;
    }

    public void reduceDodgeChance(double factor) {
        dodgeChance = dodgeChance * factor;
        if (dodgeChance < 0) dodgeChance = 0;
    }
}

class Dragon extends Monster {
    public Dragon(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Dragon(Dragon other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}

class Exoskeleton extends Monster {
    public Exoskeleton(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Exoskeleton(Exoskeleton other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}

class Spirit extends Monster {
    public Spirit(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Spirit(Spirit other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}

// ---------------- PARTY ----------------

class Party {
    private final List<Hero> heroes;

    public Party(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public boolean hasAliveHero() {
        for (Hero h : heroes) {
            if (h.isAlive()) return true;
        }
        return false;
    }

    public int getMaxLevel() {
        int max = 1;
        for (Hero h : heroes) {
            if (h.getLevel() > max) max = h.getLevel();
        }
        return max;
    }

    public Hero getRandomAliveHero(Random random) {
        List<Hero> alive = new ArrayList<>();
        for (Hero h : heroes) {
            if (h.isAlive()) alive.add(h);
        }
        if (alive.isEmpty()) return null;
        return alive.get(random.nextInt(alive.size()));
    }

    public void printSummary() {
        for (Hero h : heroes) {
            System.out.printf("%s (%s) L:%d HP:%d/%d MP:%d/%d Gold:%d%n",
                    h.getName(),
                    h.getClass().getSimpleName(),
                    h.getLevel(),
                    h.getHp(), h.getMaxHp(),
                    h.getMp(), h.getMaxMp(),
                    h.getGold());
        }
    }
}
