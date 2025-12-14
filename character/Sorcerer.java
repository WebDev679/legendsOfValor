package character;

public class Sorcerer extends Hero {
    public Sorcerer(String name, int mana, int strength, int agility,
                    int dexterity, int money, int experience) {
        super(name, 1, 100, mana, strength, dexterity, agility, money, experience);
    }

    public Sorcerer(Sorcerer other) {
        this(other.name, other.maxMp, other.strength,
             other.agility, other.dexterity, other.gold, other.experience);
    }

    @Override
    protected void applyClassBonusesOnLevelUp() {
        // Favored: dexterity & agility
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);
    }

    @Override
    public Hero createCopy() {
        return new Sorcerer(this);
    }
}
