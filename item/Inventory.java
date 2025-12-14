package item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
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

    public void removePotion(Potion p) {
        potions.remove(p);
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
