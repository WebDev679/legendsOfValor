package character;

public class Warrior extends Hero {
    public Warrior(String name, int mana, int strength, int agility,
                   int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    // Copy constructor
    public Warrior(Warrior other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: strength & agility
        strength = (int) (strength * 1.05);
        agility = (int) (agility * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Warrior(this);
    }
}
