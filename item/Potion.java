package item;

import character.Hero;

import java.util.EnumSet;

public class Potion extends Item {
    private int attributeIncrease;
    private EnumSet<AttributeType> affectedAttributes;

    public Potion(String name, int price, int requiredLevel,
                  int attributeIncrease, EnumSet<AttributeType> affectedAttributes) {
        super(name, price, requiredLevel);
        this.attributeIncrease = attributeIncrease;
        this.affectedAttributes = affectedAttributes;
    }

    public Potion(Potion other) {
        this(other.name, other.price, other.requiredLevel,
             other.attributeIncrease, other.affectedAttributes.clone());
    }

    public int getAttributeIncrease(){
        return this.attributeIncrease;
    }

    public void applyTo(Hero hero) {
        for (AttributeType attr : affectedAttributes) {
            switch (attr) {
                case AttributeType.HEALTH:
                    hero.setMaxHp(hero.getMaxHp() + this.attributeIncrease);
                    hero.heal(attributeIncrease);
                    break;
                case AttributeType.MANA:
                    hero.setMaxMp(hero.getMaxMp() + this.attributeIncrease);
                    hero.setMp(attributeIncrease);
                    if (hero.getMp() > hero.getMaxMp()) hero.setMp(hero.getMaxMp());
                    break;
                case AttributeType.STRENGTH:
                    hero.setStrength(hero.getStrength() + this.attributeIncrease);
                    break;
                case AttributeType.DEXTERITY:
                    hero.setDexterity(hero.getDexterity() + this.attributeIncrease);
                    break;
                case AttributeType.DEFENSE:
                    break;
                case AttributeType.AGILITY:
                    hero.setAgility(hero.getAgility() + this.attributeIncrease);
                    break;
            }
        }
    }
}
