package combat.action;

import character.Hero;
import item.Potion;

public class PotionAction implements HeroAction {
    private final Hero hero;
    private final Potion potion;

    public PotionAction(Hero hero, Potion potion) {
        this.hero = hero;
        this.potion = potion;
    }

    public Hero getHero() {
        return hero;
    }

    public Potion getPotion() {
        return potion;
    }
}