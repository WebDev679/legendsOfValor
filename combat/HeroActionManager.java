package combat;
import character.Hero;
import combat.action.HeroAction;

/**
 * Strategy interface for determining what action a hero takes
 * for their turn
 *
 * <p>Implementatoins encapsulate how hero makes decisions,
 * allowing combat system to be decoupled from player input, AI logic
 * or tests.</p>
 *
 * <p>Current implementations are</p>
 * <ol>
 *     <li>{@link PlayerActionManager} - prompts user for battle related inputs</li>
 *     <li>{@link combat.test.AutoAction} Autoselect actions for testing</li>
 * </ol>
 *
 * <p>Combat engine calls {@link #nextAction(Hero)} once per
 * living hero in every round</p>
 */
public interface HeroActionManager{
    HeroAction nextAction(Hero hero);
}