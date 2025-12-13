package combat.action;

import character.Monster;

public class AttackAction implements HeroAction {
    private final Monster target;
    public AttackAction(Monster target){
        this.target = target;
    }

    public Monster getTarget(){
        return target;
    }
}
