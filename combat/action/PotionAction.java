package combat.action;

import character.Hero;
import item.Potion;

/**
 * Represents action where hero consumes a potion.
 *
 * <p>{@code PotionAction} encapsulates action to use a specific potion from hero's
 * inventory. Action itself doesn't modify hero stats or inventory state.</p>
 *
 * <p>Execution is handled by combat system such as {@code ValorBattle} which applies
 * potion effects to hero and removes potoin from inventory</p>
 *
 * <p>This allows potion use to be uniformly treated with other hero actions and suports player controlled
 * and AI action selection</p>
 */
public class PotionAction implements HeroAction {
    /** Hero using the potion */
    private final Hero hero;
    /** Potion being consumed */
    private final Potion potion;

    /**
     *  Creates new potion use action
     * @param hero hero who consumes potion
     * @param potion potion to be used
     */
    public PotionAction(Hero hero, Potion potion) {
        this.hero = hero;
        this.potion = potion;
    }

    /**
     * Returns hero using potion
     * @return hero performing action
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Returns potion being consumed
     * @return potion being consumed
     */
    public Potion getPotion() {
        return potion;
    }
}