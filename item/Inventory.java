package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Armor> armors = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();
    private final List<Artifact> artifacts = new ArrayList<>();

    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public List<Weapon> getWeapons() { return weapons; }
    public List<Armor> getArmors() { return armors; }
    public List<Potion> getPotions() { return potions; }
    public List<Spell> getSpells() { return spells; }
    public List<Artifact> getArtifacts() { return artifacts; }

    /**
     * Returns an unmodifiable view of all items of the requested category.
     * <p>
     * The returned list reflects future changes to the inventory but
     * cannot be modified directly by callers.
     * </p>
     */
    public List<? extends Item> getItemsByCategory(ItemCategory category) {
        switch (category) {
            case WEAPON:
                return Collections.unmodifiableList(weapons);
            case ARMOR:
                return Collections.unmodifiableList(armors);
            case POTION:
                return Collections.unmodifiableList(potions);
            case SPELL:
                return Collections.unmodifiableList(spells);
            case ARTIFACT:
                return Collections.unmodifiableList(artifacts);
            default:
                return Collections.emptyList();
        }
    }

    public int getWeaponCount() { return weapons.size(); }
    public int getArmorCount() { return armors.size(); }
    public int getPotionCount() { return potions.size(); }
    public int getSpellCount() { return spells.size(); }

    public void addWeapon(Weapon w) { weapons.add(w); }
    public void addArmor(Armor a) { armors.add(a); }
    public void addPotion(Potion p) { potions.add(p); }
    public void addSpell(Spell s) { spells.add(s); }
    public void addArtifact(Artifact a) { artifacts.add(a); }

    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }

    public void equipWeapon(int index) {
        if (index >= 0 && index < weapons.size()) {
            equippedWeapon = weapons.get(index);
        }
    }

    public void equipArmor(int index) {
        if (index >= 0 && index < armors.size()) {
            equippedArmor = armors.get(index);
        }
    }

    public void removePotion(Potion p) {
        potions.remove(p);
    }

    /**
     * Returns true if the given item instance is present in this inventory.
     */
    public boolean contains(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Weapon) {
            return weapons.contains(item);
        }
        if (item instanceof Armor) {
            return armors.contains(item);
        }
        if (item instanceof Potion) {
            return potions.contains(item);
        }
        if (item instanceof Spell) {
            return spells.contains(item);
        }
        if (item instanceof Artifact) {
            return artifacts.contains(item);
        }
        return false;
    }

    /**
     * Removes the given item instance from the appropriate internal list.
     *
     * @param item the item to remove
     * @return true if the item was present and removed; false otherwise
     */
    public boolean removeItem(Item item) {
        if (item == null) {
            return false;
        }

        if (item instanceof Weapon) {
            return weapons.remove(item);
        }
        if (item instanceof Armor) {
            return armors.remove(item);
        }
        if (item instanceof Potion) {
            return potions.remove(item);
        }
        if (item instanceof Spell) {
            return spells.remove(item);
        }
        if (item instanceof Artifact) {
            return artifacts.remove(item);
        }
        return false;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Weapons:\n");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            sb.append(String.format("  [%d] %s (Dmg:%d, Hands:%d, Lvl:%d)%n",
                    i, w.getName(), w.getDamage(), w.getHandsRequired(), w.getRequiredLevel()));
        }
        sb.append("Armors:\n");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            sb.append(String.format("  [%d] %s (Red:%d, Lvl:%d)%n",
        i, a.getName(), a.getDamageReduction(), a.getRequiredLevel()));

        }
        sb.append("Potions:\n");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            sb.append(String.format("  [%d] %s (Inc:%d, Lvl:%d)%n",
                    i, p.getName(), p.getAttributeIncrease(), p.getRequiredLevel()));
        }
        sb.append("Spells:\n");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            sb.append(String.format("  [%d] %s (%s Dmg:%d, MP:%d, Lvl:%d)%n",
                    i, s.getName(), s.getType(), s.getDamage(), s.getManaCost(), s.getRequiredLevel()));
        }
        sb.append("Artifacts:\n");
        for (int i = 0; i < artifacts.size(); i++) {
            Artifact a = artifacts.get(i);
            sb.append(String.format("  [%d] %s (Price:%d, Lvl:%d)%n",
                    i, a.getName(), a.getPrice(), a.getRequiredLevel()));
        }
        return sb.toString();
    }
}
