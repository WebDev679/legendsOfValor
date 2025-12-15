package item;

public class Armor extends Item {
    private int damageReduction;

    public Armor(String name, int price, int requiredLevel, int damageReduction) {
        super(name, price, requiredLevel);
        this.damageReduction = damageReduction;
    }

    public Armor(Armor other) {
        this(other.name, other.price, other.requiredLevel, other.damageReduction);
    }

    public int getDamageReduction() { return damageReduction; }

    @Override
    public void addToInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }
        inventory.addArmor(new Armor(this));
    }
}
