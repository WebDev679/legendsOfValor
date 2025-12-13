package combat.action;
import character.Monster;
import item.Spell;

public class SpellcastAction implements HeroAction{
    private final Spell spell;
    private final Monster target;

    public SpellcastAction(Spell spell, Monster target) {
        this.spell = spell;
        this.target = target;
    }

    public Spell getSpell() {
        return spell;
    }

    public Monster getTarget() {
        return target;
    }
}
