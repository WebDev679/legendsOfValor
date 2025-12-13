package character;// character.Character.java

import world.Position;

public abstract class Character {
    protected String name;
    protected int level;
    protected int hp;
    protected int maxHp;
    protected Position position;

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

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

