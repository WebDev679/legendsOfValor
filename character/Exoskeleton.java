package character;

public class Exoskeleton extends Monster {
    public Exoskeleton(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Exoskeleton(Exoskeleton other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}
