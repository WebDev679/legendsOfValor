package combat;
import character.Hero;
import character.Monster;

public final class ValorCombatExecutor {
    private ValorCombatExecutor(){}

    public static void monsterAttack(Monster monster, Hero hero){
        if (Math.random() < hero.getDodgeChance()){
            System.out.println(hero.getName() + "dodged attack from " + monster.getName());
            return;
        }

        int damage = DamageCalculator.monsterDamage(monster, hero);

        hero.takeDamage(damage);

        System.out.printf(
                "%s hits %s for %d damage.%n",
                monster.getName(), hero.getName(), damage
        );
    }

}
