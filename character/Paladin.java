package character;

public class Paladin extends Hero {
    public Paladin(String name, int mana, int strength, int agility,
                   int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    public Paladin(Paladin other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: strength & dexterity
        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Paladin(this);
    }
}
