package combat.action;
import character.Hero;
import character.Monster;
import item.Spell;

/**
 * Represents action where hero casts spell on target monster.
 *
 * <p>{@code SpellcastAction} encapsulates all data required to
 * perform a spell attack: including casting hero, spell being cast and
 * target monster.</p>
 *
 * <p>The class doesn't apply damage, deduct mana or enforce combat rules. These
 * responsibilities are handled by combat engine.</p>
 *
 * <p>Seperating spell intent from execution allows Spellcasting to be
 * similarly treated to other hero actiouns and supports playr controlled and
 * AI decisions</p>
 */
public class SpellcastAction implements HeroAction{
    /** Spell being cast */
    private final Spell spell;
    /** Monster being targeted */
    private final Monster target;
    /** Hero casting the spell */
    private final Hero hero;

    /**
     * Generates new spellcast action
     * @param hero hero casting spell
     * @param spell spell to cast
     * @param target monster being targeted by spell
     */
    public SpellcastAction(Hero hero, Spell spell, Monster target) {
        this.spell = spell;
        this.target = target;
        this.hero = hero;
    }

    /**
     * Returns spell being cast
     * @return the spell
     */
    public Spell getSpell() {
        return spell;
    }

    /**
     * REturns target monster
     * @return monster being targeted
     */
    public Monster getTarget() {
        return target;
    }

    /**
     * Returns hero casting the spell
     * @return casting hero
     */
    public Hero getHero() {
        return hero;
    }
}
