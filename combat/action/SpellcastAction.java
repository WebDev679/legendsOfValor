package combat.action;
import character.Hero;
import character.Monster;
import item.Spell;

public class SpellcastAction implements HeroAction{
    private final Spell spell;
    private final Monster target;
    private final Hero hero;

    public SpellcastAction(Hero hero, Spell spell, Monster target) {
        this.spell = spell;
        this.target = target;
        this.hero = hero;
    }

    public Spell getSpell() {
        return spell;
    }

    public Monster getTarget() {
        return target;
    }

    public Hero getHero() {
        return hero;
    }
}
