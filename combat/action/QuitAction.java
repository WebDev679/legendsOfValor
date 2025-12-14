package combat.action;

/**
 * Represents player request to quit ongoing battle.
 * <p>Thisis a marker action that doesn't contain any data.
 * This is used as a signal by combat system to terminate battle early.</p>
 *
 * <p>This is interpreted by battle controller, throwing a {@code QuitBattleException} to
 * stop battle flow and transition to a {@code GameOverState} or other state as required.</p>
 *
 * <p>Using an explicit class rather than directly terminationg helps to separate concerns</p>
 */
public final class QuitAction implements HeroAction {
}
