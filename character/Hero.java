package character;

import item.*;
import combat.StatCalculator;

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
        while (experience >= StatCalculator.expToNextLevel(this)) {
            experience -= StatCalculator.expToNextLevel(this);
            levelUp();
        }
    }

    protected void baseLevelUp() {
        level++;
        maxHp = StatCalculator.newMaxHpAfterLevelUp(this);
        hp = maxHp;

        maxMp = StatCalculator.newMaxMpAfterLevelUp(this);
        mp = maxMp;

        strength = StatCalculator.newStatAfterLevelUp(strength);
        dexterity = StatCalculator.newStatAfterLevelUp(dexterity);
        agility = StatCalculator.newStatAfterLevelUp(agility);
    }

    protected abstract void applyClassBonusesOnLevelUp();

    public void levelUp() {
        baseLevelUp();
        applyClassBonusesOnLevelUp();
        System.out.println(name + " leveled up to level " + level + "!");
    }

    public double getDodgeChance() {
        return StatCalculator.dodgeChanceFromAgility(agility);
    }

    public boolean spendMana(int cost) {
        if (mp < cost) return false;
        mp -= cost;
        return true;
    }

    public void regenAfterRound() {
        if (!isAlive()) return;
        hp = StatCalculator.regen10Percent(hp);
        if (hp > maxHp){
            System.out.println("Hero " + this.getName() + " recovered health from " + hp + " --> " + maxHp);
            hp = maxHp;
        }
        mp = StatCalculator.regen10Percent(mp);
        if (mp > maxMp){
            System.out.println("Hero " + this.getName() + " recovered mana from " + mp + " --> " + maxMp);
            mp = maxMp;
        }
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
                gold, experience, StatCalculator.expToNextLevel(this)));
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
    public void setHp(int hp) { this.hp = hp; }

    public abstract Hero createCopy();
}
