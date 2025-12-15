package combat.action;

import character.Hero;

/**
 * Represents action when a hero explicitly skips turn
 *
 * <p>Used when hero chooses to not perform any combat or other action during
 * turn. This can occur due to player choice, invalid input or lack of actions or automated
 * decision log</p>
 *
 * <p>Action doesn't modify game state itself. It signals combat engine that the hero turn is complete
 * without executing any attack, spell, etc.</p>
 *
 * <p>Having a defined skip action instead of implicit no-op helps to improve clarity and avoid unintentional no-ops.
 * Also ensures that all heroes peform exactly one valid action per turn</p>
 */
public class SkipAction implements HeroAction{
    /** Hero who is skipping turn */
    private Hero hero;

    /**
     * Create a skip action for given hero
     * @param hero the hero skipping turn
     */
    public SkipAction(Hero hero){
        this.hero = hero;
    }

    /**
     * Returns hero associated with skip action
     * @return hero skipping turn
     */
    public Hero getHero(){
        return hero;
    }
}
