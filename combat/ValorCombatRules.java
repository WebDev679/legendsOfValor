package combat;
import character.Character;
import world.Position;

public final class ValorCombatRules {
    private ValorCombatRules() {}

    public static boolean canAttack(Character attacker, Character defender) {
        Position a = attacker.getPosition();
        Position d = defender.getPosition();
        return RangeCheck.inRange(a, d);
    }
}
