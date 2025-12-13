package character;

import item.*;

public abstract class Hero extends Character {
    public int mp;
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
        sb.append("\nitem.Inventory:\n");
        sb.append(inventory.describe());
        return sb.toString();
    }

    public void setMp(int mp) { this.mp = mp; }
    public void setMaxMp(int maxMp) { this.maxMp = maxMp; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public void setAgility(int agility) { this.agility = agility; }
    public void setGold(int gold) { this.gold = gold; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public void setHp(int hp) { this.mp = hp; }

    public abstract Hero createCopy();
}
