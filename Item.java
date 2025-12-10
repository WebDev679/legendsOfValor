// Item.java
import java.util.*;

public abstract class Item {
    protected String name;
    protected int price;
    protected int requiredLevel;

    public Item(String name, int price, int requiredLevel) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getRequiredLevel() { return requiredLevel; }
}

// ---------- Weapon ----------

class Weapon extends Item {
    private int damage;
    private int handsRequired;

    public Weapon(String name, int price, int requiredLevel, int damage, int handsRequired) {
        super(name, price, requiredLevel);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public Weapon(Weapon other) {
        this(other.name, other.price, other.requiredLevel, other.damage, other.handsRequired);
    }

    public int getDamage() { return damage; }
    public int getHandsRequired() { return handsRequired; }
}

// ---------- Armor ----------

class Armor extends Item {
    private int damageReduction;

    public Armor(String name, int price, int requiredLevel, int damageReduction) {
        super(name, price, requiredLevel);
        this.damageReduction = damageReduction;
    }

    public Armor(Armor other) {
        this(other.name, other.price, other.requiredLevel, other.damageReduction);
    }

    public int getDamageReduction() { return damageReduction; }
}

// ---------- Potion ----------

enum AttributeType {
    HEALTH, MANA, STRENGTH, DEXTERITY, DEFENSE, AGILITY
}

class Potion extends Item {
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
                case HEALTH:
                    hero.maxHp += attributeIncrease;
                    hero.heal(attributeIncrease);
                    break;
                case MANA:
                    hero.maxMp += attributeIncrease;
                    hero.mp += attributeIncrease;
                    if (hero.mp > hero.maxMp) hero.mp = hero.maxMp;
                    break;
                case STRENGTH:
                    hero.strength += attributeIncrease;
                    break;
                case DEXTERITY:
                    hero.dexterity += attributeIncrease;
                    break;
                case DEFENSE:
                    break;
                case AGILITY:
                    hero.agility += attributeIncrease;
                    break;
            }
        }
    }
}

// ---------- Spell ----------

enum SpellType {
    FIRE, ICE, LIGHTNING
}

class Spell extends Item {
    private int damage;
    private int manaCost;
    private SpellType type;

    public Spell(String name, int price, int requiredLevel,
                 int damage, int manaCost, SpellType type) {
        super(name, price, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public Spell(Spell other) {
        this(other.name, other.price, other.requiredLevel,
             other.damage, other.manaCost, other.type);
    }

    public int getDamage() { return damage; }
    public int getManaCost() { return manaCost; }
    public SpellType getType() { return type; }

    public void applyEffect(Monster target) {
        switch (type) {
            case FIRE:
                target.reduceDefense(0.9); // reduce defense by 10%
                break;
            case ICE:
                target.reduceDamage(0.9); // reduce damage by 10%
                break;
            case LIGHTNING:
                target.reduceDodgeChance(0.9); // reduce dodge chance by 10%
                break;
        }
    }
}

// ---------- Inventory ----------

class Inventory {
    private List<Weapon> weapons = new ArrayList<>();
    private List<Armor> armors = new ArrayList<>();
    private List<Potion> potions = new ArrayList<>();
    private List<Spell> spells = new ArrayList<>();

    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public List<Weapon> getWeapons() { return weapons; }
    public List<Armor> getArmors() { return armors; }
    public List<Potion> getPotions() { return potions; }
    public List<Spell> getSpells() { return spells; }

    public void addWeapon(Weapon w) { weapons.add(w); }
    public void addArmor(Armor a) { armors.add(a); }
    public void addPotion(Potion p) { potions.add(p); }
    public void addSpell(Spell s) { spells.add(s); }

    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }

    public void equipWeapon(int index) {
        if (index >= 0 && index < weapons.size()) {
            equippedWeapon = weapons.get(index);
        }
    }

    public void equipArmor(int index) {
        if (index >= 0 && index < armors.size()) {
            equippedArmor = armors.get(index);
        }
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Weapons:\n");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            sb.append(String.format("  [%d] %s (Dmg:%d, Hands:%d, Lvl:%d)%n",
                    i, w.getName(), w.getDamage(), w.getHandsRequired(), w.getRequiredLevel()));
        }
        sb.append("Armors:\n");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            sb.append(String.format("  [%d] %s (Red:%d, Lvl:%d)%n",
        i, a.getName(), a.getDamageReduction(), a.getRequiredLevel()));

        }
        sb.append("Potions:\n");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            sb.append(String.format("  [%d] %s (Inc:%d, Lvl:%d)%n",
                    i, p.getName(), p.getAttributeIncrease(), p.getRequiredLevel()));
        }
        sb.append("Spells:\n");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            sb.append(String.format("  [%d] %s (%s Dmg:%d, MP:%d, Lvl:%d)%n",
                    i, s.getName(), s.getType(), s.getDamage(), s.getManaCost(), s.getRequiredLevel()));
        }
        return sb.toString();
    }
}
