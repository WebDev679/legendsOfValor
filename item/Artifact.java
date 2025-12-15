package item;

/**
 * Legends of Valor specific artifact item.
 *
 * <p>An {@code Artifact} is a generic extension point for special items
 * that do not fit cleanly into the core weapon/armor/potion/spell
 * categories. Artifacts can still be bought and sold in markets using
 * the shared {@link engine.MarketService} rules.</p>
 */
public class Artifact extends Item {

    private String description;

    public Artifact(String name, int price, int requiredLevel, String description) {
        super(name, price, requiredLevel);
        this.description = description;
    }

    public Artifact(Artifact other) {
        this(other.name, other.price, other.requiredLevel, other.description);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void addToInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }
        inventory.addArtifact(new Artifact(this));
    }
}


