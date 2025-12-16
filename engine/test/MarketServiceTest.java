package engine.test;

import character.Hero;
import character.Warrior;
import engine.MarketService;
import item.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Simple assertion-based tests for {@link MarketService} and inventory flows.
 *
 * <p>These tests are intended to be run manually via {@link #main(String[])}
 * or from a larger test harness. They use Java assertions, so remember to
 * enable them with the {@code -ea} JVM flag.</p>
 */
public class MarketServiceTest {

    public static void main(String[] args) {
        run();
        System.out.println("✓ MarketServiceTest passed");
    }

    public static void run() {
        testBuyAndSellWeapon();
        testBuyAndSellArtifact();
        testCannotBuyWhenInsufficientGold();
    }

    private static MarketService createMarketWithItems(List<Item> extraItems) {
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon("TestSword", 100, 1, 20, 1));

        List<Armor> armors = new ArrayList<Armor>();
        armors.add(new Armor("TestArmor", 80, 1, 10));

        EnumSet<AttributeType> attrs = EnumSet.of(AttributeType.HEALTH);
        List<Potion> potions = new ArrayList<Potion>();
        potions.add(new Potion("TestPotion", 50, 1, 5, attrs));

        List<Spell> spells = new ArrayList<Spell>();
        spells.add(new Spell("TestSpell", 120, 1, 40, 20, SpellType.FIRE));

        List<Artifact> artifacts = new ArrayList<Artifact>();
        artifacts.add(new Artifact("TestAmulet", 150, 1, "Simple test artifact"));

        if (extraItems != null) {
            for (Item item : extraItems) {
                if (item instanceof Artifact) {
                    artifacts.add((Artifact) item);
                }
            }
        }

        return new MarketService(weapons, armors, potions, spells, artifacts);
    }

    private static Hero createHeroWithGold(int gold) {
        Hero hero = new Warrior("Tester", 100, 20, 10, 10, gold, 0);
        return hero;
    }

    private static void testBuyAndSellWeapon() {
        MarketService market = createMarketWithItems(null);
        Hero hero = createHeroWithGold(500);

        Weapon sword = market.getWeaponPrototypes().get(0);
        assert market.canBuy(hero, sword) : "Hero should be able to buy weapon";

        int goldBefore = hero.getGold();
        market.buy(hero, sword);
        assert hero.getGold() == goldBefore - sword.getPrice()
                : "Gold not deducted correctly on weapon purchase";
        assert hero.getInventory().getWeapons().size() == 1
                : "Weapon not added to inventory";

        Weapon ownedSword = hero.getInventory().getWeapons().get(0);
        assert market.canSell(hero, ownedSword) : "Hero should be able to sell owned weapon";

        int goldBeforeSell = hero.getGold();
        market.sell(hero, ownedSword);
        int expectedGoldAfterSell = goldBeforeSell + sword.getPrice() / 2;
        assert hero.getGold() == expectedGoldAfterSell
                : "Gold not increased correctly on weapon sale";
        assert hero.getInventory().getWeapons().isEmpty()
                : "Weapon should be removed from inventory after sale";
    }

    private static void testBuyAndSellArtifact() {
        MarketService market = createMarketWithItems(null);
        Hero hero = createHeroWithGold(500);

        Artifact artifact = market.getArtifactPrototypes().get(0);
        assert market.canBuy(hero, artifact) : "Hero should be able to buy artifact";

        int goldBefore = hero.getGold();
        market.buy(hero, artifact);
        assert hero.getGold() == goldBefore - artifact.getPrice()
                : "Gold not deducted correctly on artifact purchase";
        assert hero.getInventory().getArtifacts().size() == 1
                : "Artifact not added to inventory";

        Artifact owned = hero.getInventory().getArtifacts().get(0);
        assert market.canSell(hero, owned) : "Hero should be able to sell owned artifact";

        int goldBeforeSell = hero.getGold();
        market.sell(hero, owned);
        int expectedGoldAfterSell = goldBeforeSell + artifact.getPrice() / 2;
        assert hero.getGold() == expectedGoldAfterSell
                : "Gold not increased correctly on artifact sale";
        assert hero.getInventory().getArtifacts().isEmpty()
                : "Artifact should be removed from inventory after sale";
    }

    private static void testCannotBuyWhenInsufficientGold() {
        MarketService market = createMarketWithItems(null);
        Hero hero = createHeroWithGold(10); // not enough for most items

        Weapon sword = market.getWeaponPrototypes().get(0);
        assert !market.canBuy(hero, sword)
                : "Hero with insufficient gold should not be able to buy weapon";
    }
}


