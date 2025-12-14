package combat.action;

import character.Hero;
import character.Monster;

public class AttackAction implements HeroAction {
    private final Monster target;
    private final Hero attacker;
    public AttackAction(Hero attacker, Monster target){
        this.target = target;
        this.attacker = attacker;
    }

    public Monster getTarget(){
        return target;
    }

    public Hero getAttacker(){
        return attacker;
    }
}
