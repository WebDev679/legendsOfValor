package combat.action;

import character.Hero;

/**
 * Represents a hero changing their equipped weapon or armor during combat.
 *
 * <p>The {@code EquipAction} itself does not modify inventory; it simply
 * captures the hero, target slot, and chosen index. {@link combat.ValorBattle}
 * interprets this action and applies the change, which keeps game rules
 * separated from player input.</p>
 */
public class EquipAction implements HeroAction {

    public enum Slot {
        WEAPON,
        ARMOR
    }

    private final Hero hero;
    private final Slot slot;
    private final int itemIndex;

    public EquipAction(Hero hero, Slot slot, int itemIndex) {
        this.hero = hero;
        this.slot = slot;
        this.itemIndex = itemIndex;
    }

    public Hero getHero() {
        return hero;
    }

    public Slot getSlot() {
        return slot;
    }

    public int getItemIndex() {
        return itemIndex;
    }
}


