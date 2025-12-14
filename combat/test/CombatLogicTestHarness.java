package combat.test;

public class CombatLogicTestHarness {
    public static void main(String[] args) {
        System.out.println("Running test harness for combat logic");

        DamageCalculatorTest.run();
        SpellTest.run();

        System.out.println("Done running test harness for combat logic, all tests passed!");
    }
}
