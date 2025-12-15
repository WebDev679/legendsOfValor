package combat;
import character.Character;
import world.Position;

/**
 * Encapsulates combat related rule checks for legends of valor
 * <p>Defines whether an action is allowed, but doesn't execute action itself. This is intentionally
 * separated from {@link ValorCombatExecutor} to keep rule validation decoupled from combat side effects.</p>
 *
 * <p>All methods are static as rules are global and stateless</p>
 */
public final class ValorCombatRules {
    /** Prevent instances of utility class */
    private ValorCombatRules() {}

    /**
     * Determines whether one character can attack another based on position
     * <p>Attack allowed only if defender in attack range of attacker. Range logic is in {@link RangeCheck},
     * allowing combat rules to be independent of map geometry</p>
     * @param attacker character tryingto attack
     * @param defender prospective defender
     * @return ture if defender in attack range, otherwise false
     */
    public static boolean canAttack(Character attacker, Character defender) {
        Position a = attacker.getPosition();
        Position d = defender.getPosition();
        return RangeCheck.inRange(a, d);
    }
}
