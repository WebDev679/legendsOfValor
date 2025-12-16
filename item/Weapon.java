package item;

public class Weapon extends Item {
    private int damage;
    private int handsRequired;

    public Weapon(String name, int price, int requiredLevel, int damage, int handsRequired) {
        super(name, price, requiredLevel);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public Weapon(Weapon other) {
        this(other.name, other.price, other.requiredLevel, other.damage, other.handsRequired);
    }

    public int getDamage() { return damage; }
    public int getHandsRequired() { return handsRequired; }

    @Override
    public void addToInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }
        inventory.addWeapon(new Weapon(this));
    }
}
