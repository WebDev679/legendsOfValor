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
}
