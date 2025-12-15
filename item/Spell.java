package item;

import character.Monster;

public class Spell extends Item {
    private int damage;
    private int manaCost;
    private SpellType type;

    public Spell(String name, int price, int requiredLevel,
                 int damage, int manaCost, SpellType type) {
        super(name, price, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public Spell(Spell other) {
        this(other.name, other.price, other.requiredLevel,
             other.damage, other.manaCost, other.type);
    }

    public int getDamage() { return damage; }
    public int getManaCost() { return manaCost; }
    public SpellType getType() { return type; }

    @Override
    public void addToInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }
        inventory.addSpell(new Spell(this));
    }

    public void applyEffect(Monster target) {
        switch (type) {
            case FIRE:
                target.reduceDefense(0.9); // reduce defense by 10%
                break;
            case ICE:
                target.reduceDamage(0.9); // reduce damage by 10%
                break;
            case LIGHTNING:
                target.reduceDodgeChance(0.9); // reduce dodge chance by 10%
                break;
        }
    }
}
