package combat;
import character.Hero;
import character.Monster;
import item.Armor;

public final class ValorCombatExecutor {
    private ValorCombatExecutor(){}

    public static void monsterAttack(Monster monster, Hero hero){
        if (Math.random() < hero.getDodgeChance()){
            System.out.println(hero.getName() + "dodged attack from " + monster.getName());
            return;
        }

        int damage = calculateMonsterDamage(monster, hero);
        hero.takeDamage(damage);

        System.out.printf(
                "%s hits %s for %d damage.%n",
                monster.getName(), hero.getName(), damage
        );
    }

    private static int calculateMonsterDamage(Monster monster, Hero hero) {
        int armorRed = 0;
        Armor armor = hero.getEquippedArmor();
        if (armor != null){
            armorRed = armor.getDamageReduction();
        }

        double baseDamage = monster.getDamage() * 0.05;
        double factor = 100.0 / (100.0 + armorRed);
        return Math.max(0, (int)(baseDamage * factor));
    }
}
