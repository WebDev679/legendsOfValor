package combat.action;

import character.Hero;
import character.Monster;

/**
 * Representation of basic physical attack action by a hero against a monster.
 *
 * <p>An {@code AttackAction} is a data object to capture attack intent: including
 * attacking hero and chosen target monster. Action doesn't apply damage or enforce combat rules itself</p>
 *
 * <p>Execution is handled by combat system such as {@link combat.ValorBattle} and {@link combat.ValorCombatExecutor}, which validate
 * attack conditions and calculate damage.</p>
 *
 * <p>This allows for different action sources to have identical attack actions, without a neeed for
 * duplicating the combat logic</p>
 */
public class AttackAction implements HeroAction {
    /** Hero performing the attack. */
    private final Monster target;
    /** Monster being targeted in attack */
    private final Hero attacker;

    /**
     * Constructor to create new attack action
     * @param attacker hero performing attack
     * @param target monster being attacked
     */
    public AttackAction(Hero attacker, Monster target){
        this.target = target;
        this.attacker = attacker;
    }

    /**
     * Returns target of attack
     * @return target monster
     */
    public Monster getTarget(){
        return target;
    }

    /**
     * Returns hero performing attack
     * @return attacking hero
     */
    public Hero getAttacker(){
        return attacker;
    }
}
