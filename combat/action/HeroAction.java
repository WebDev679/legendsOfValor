package combat.action;
import character.Monster;

/**
 * Interface to represent a single action
 * done by a hero during a turn.
 *
 * <p>{@code HeroAction} encapsulates all data required to resolve a
 * hero's turn without directly running game logic. Actions are interpreted and
 * executed by combat system such as {@link combat.ValorBattle} and not by action itself</p>
 *
 * <p>Implementations include:</p>
 * <ul>
 *     <li>{@link AttackAction}</li>
 *     <li>{@link PotionAction}</li>
 *     <li>{@link QuitAction}</li>
 *     <li>{@link SkipAction}</li>
 *     <li>{@link SpellcastAction}</li>
 * </ul>
 *
 * <p>Theinterface design separates decision making from execution.
 *  This allows for action sources to be player input, AI or automated test scripts</p>
 */
public interface HeroAction{}