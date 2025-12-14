package combat.action;

import character.Hero;

public class SkipAction implements HeroAction{
    private Hero hero;

    public SkipAction(Hero hero){
        this.hero = hero;
    }

    public Hero getHero(){
        return hero;
    }
}
