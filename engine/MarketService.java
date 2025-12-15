package engine;

import character.Hero;
import item.Armor;
import item.Artifact;
import item.Item;
import item.Potion;
import item.Spell;
import item.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pure domain service for marketplace rules.
 *
 * <p>This class encapsulates buying and selling logic for all items without
 * performing any input/output. It operates on {@link Hero} and their
 * {@link item.Inventory} only, making it reusable from both the legacy
 * Monsters &amp; Heroes market UI and the Legends of Valor nexus markets.</p>
 */
public class MarketService {

    private final List<Weapon> weaponPrototypes;
    private final List<Armor> armorPrototypes;
    private final List<Potion> potionPrototypes;
    private final List<Spell> spellPrototypes;
    private final List<Artifact> artifactPrototypes;

    /**
     * Creates a new {@code MarketService}.
     *
     * @param weapons   weapon prototypes available for purchase
     * @param armors    armor prototypes available for purchase
     * @param potions   potion prototypes available for purchase
     * @param spells    spell prototypes (any type) available for purchase
     * @param artifacts optional list of Legends of Valor specific artifacts
     */
    public MarketService(
            List<Weapon> weapons,
            List<Armor> armors,
            List<Potion> potions,
            List<Spell> spells,
            List<Artifact> artifacts
    ) {
        this.weaponPrototypes = weapons != null ? weapons : new ArrayList<>();
        this.armorPrototypes = armors != null ? armors : new ArrayList<>();
        this.potionPrototypes = potions != null ? potions : new ArrayList<>();
        this.spellPrototypes = spells != null ? spells : new ArrayList<>();
        this.artifactPrototypes = artifacts != null ? artifacts : new ArrayList<>();
    }

    public List<Weapon> getWeaponPrototypes() {
        return Collections.unmodifiableList(weaponPrototypes);
    }

    public List<Armor> getArmorPrototypes() {
        return Collections.unmodifiableList(armorPrototypes);
    }

    public List<Potion> getPotionPrototypes() {
        return Collections.unmodifiableList(potionPrototypes);
    }

    public List<Spell> getSpellPrototypes() {
        return Collections.unmodifiableList(spellPrototypes);
    }

    public List<Artifact> getArtifactPrototypes() {
        return Collections.unmodifiableList(artifactPrototypes);
    }

    /**
     * Returns whether the given hero can afford and is allowed to buy the item.
     */
    public boolean canBuy(Hero hero, Item item) {
        if (hero == null || item == null) {
            return false;
        }
        return hero.getLevel() >= item.getRequiredLevel()
                && hero.getGold() >= item.getPrice();
    }

    /**
     * Applies the purchase side effects for the given item.
     *
     * <p>Callers should check {@link #canBuy(Hero, Item)} first. If the hero
     * cannot buy the item this method will throw an {@link IllegalArgumentException}.</p>
     */
    public void buy(Hero hero, Item item) {
        if (!canBuy(hero, item)) {
            throw new IllegalArgumentException("Hero cannot buy item: " + (item != null ? item.getName() : "null"));
        }

        hero.spendGold(item.getPrice());
        item.addToInventory(hero.getInventory());
    }

    /**
     * Determines whether a hero is allowed to sell a specific item instance.
     *
     * <p>At present the only rule is that the hero must own the item. Callers
     * can layer additional UI rules (e.g. preventing sale of currently
     * equipped gear) on top if desired.</p>
     */
    public boolean canSell(Hero hero, Item item) {
        if (hero == null || item == null) {
            return false;
        }
        return hero.getInventory().contains(item);
    }

    /**
     * Applies the sell side effects for the given item.
     *
     * <p>Sale price is currently half of the original purchase price, rounded
     * down. If the hero does not actually own the item an
     * {@link IllegalArgumentException} is thrown.</p>
     */
    public void sell(Hero hero, Item item) {
        if (hero == null || item == null) {
            throw new IllegalArgumentException("Hero and item must be non-null");
        }

        boolean removed = hero.getInventory().removeItem(item);
        if (!removed) {
            throw new IllegalArgumentException("Hero does not own item: " + item.getName());
        }

        int salePrice = item.getPrice() / 2;
        hero.addGold(salePrice);
    }
}


