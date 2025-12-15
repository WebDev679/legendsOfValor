package character;

public class Dragon extends Monster {
    public Dragon(String name, int level, int damage, int defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    public Dragon(Dragon other) {
        this(other.name, other.level, other.damage, other.defense, other.dodgeChance);
    }
}
