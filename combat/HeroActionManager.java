package combat;
import character.Hero;
import combat.action.HeroAction;

public interface HeroActionManager{
    HeroAction nextAction(Hero hero);
}