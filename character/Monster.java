package character;

import combat.StatCalculator;

public abstract class Monster extends Character {
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
    public double getDodgeChance() {
        return StatCalculator.monsterDodgeChance(dodgeChance);
    }

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

    public void setMaxHp(int newMaxHp) {
        this.maxHp = newMaxHp;
    }

    public void setHp(int newHp) {
        this.hp = newHp;
    }
}
