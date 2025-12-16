package combat;

/**
 * Used to signal that player has chosen to quit ongoing battle
 *
 * <p>Intentionally used as control flow mechanism to immediately exit
 * from attle loop and transition to game over state</p>
 *
 * <p>Not an error condition</p>
 */
public class QuitBattleException extends Throwable {
}
