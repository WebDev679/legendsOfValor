package character;

public class Spirit extends Monster {
    public Spirit(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Spirit(Spirit other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}
