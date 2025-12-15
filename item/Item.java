package item;// item.Item.java

public abstract class Item {
    protected String name;
    protected int price;
    protected int requiredLevel;

    public Item(String name, int price, int requiredLevel) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getRequiredLevel() { return requiredLevel; }

    /**
     * Adds a copy of this item to the given inventory.
     *
     * <p>This method is used by marketplace logic to avoid large instanceof
     * chains when buying items. Each concrete item type is responsible for
     * deciding how it should be represented in the hero's inventory.</p>
     */
    public abstract void addToInventory(Inventory inventory);
}
