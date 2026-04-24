package Units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SquadTest {
    private record ChargeAttackScenario(int difficultyClass, int successfulAttacks, int crits) {}
    private static final Integer[] SAMPLED_ENEMY_CASUALTIES = {1, 5, 10};
    private static final ChargeAttackScenario[] FAILED_CHARGE_SCENARIOS = {
            new ChargeAttackScenario(5, 0, 0),
            new ChargeAttackScenario(10, 1, 0),
            new ChargeAttackScenario(15, 1, 1),
            new ChargeAttackScenario(20, 2, 1)
    };
    private static final ChargeAttackScenario[] SUCCESSFUL_CHARGE_SCENARIOS = {
            new ChargeAttackScenario(10, 1, 0),
            new ChargeAttackScenario(15, 1, 1),
            new ChargeAttackScenario(20, 2, 1),
            new ChargeAttackScenario(25, 3, 2),
            new ChargeAttackScenario(30, 4, 3),
            new ChargeAttackScenario(35, 5, 3),
            new ChargeAttackScenario(40, 6, 4)
    };

    private static Squad createSquadWithChargeAttack(int casualties, DiceRoll chargeAttack) {
        return new Squad("Test Squad", "Test Faction", 5, 2, casualties, 1, 1, 1, createJavelinSkirmishWeapons(), chargeAttack, createSpearsMeleeWeapons());
    }

    private static HashMap<String, SkirmishWeapon> createJavelinSkirmishWeapons() {
        HashMap<String, SkirmishAttack> skirmishAttacks = new HashMap<>();
        skirmishAttacks.put("Javelin Throw", new SkirmishAttack("Javelin Throw", new DiceRoll(6, 6, 2), true, -1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", skirmishAttacks));
        return skirmishWeapons;
    }

    private static HashMap<String, MeleeWeapon> createSpearsMeleeWeapons() {
        HashMap<String, MeleeAttack> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Spears", new MeleeAttack("Spears", new DiceRoll(8, 4, 2)));

        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        meleeWeapons.put("Spears", new MeleeWeapon("Spears", meleeAttacks));
        return meleeWeapons;
    }

    private static boolean isChargeSuccessful(int squadCasualties, int successfulAttacks, int enemyCasualties) {
        return successfulAttacks * 2 >= squadCasualties || successfulAttacks > enemyCasualties;
    }

    private static Stream<Arguments> sampledChargeAttackCases(ChargeAttackScenario[] scenarios, boolean expectedSuccess) {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .flatMap(squadCasualties -> Stream.of(SAMPLED_ENEMY_CASUALTIES)
                        .map(enemyCasualties -> {
                            ChargeAttackScenario[] validScenarios = Arrays.stream(scenarios)
                                    .filter(scenario -> scenario.successfulAttacks() <= squadCasualties)
                                    .filter(scenario -> scenario.crits() <= scenario.successfulAttacks())
                                    .filter(scenario -> isChargeSuccessful(squadCasualties, scenario.successfulAttacks(), enemyCasualties) == expectedSuccess)
                                    .toArray(ChargeAttackScenario[]::new);

                            // We intentionally sample one scenario per (squadCasualties, enemyCasualties) pair
                            // instead of running every valid scenario combination. Each source therefore produces
                            // 10 squad sizes x 3 enemy casualty samples = 30 stable cases, with this modulo-based
                            // selection rotating through the valid DC/success/crit patterns deterministically.
                            if (validScenarios.length == 0) {
                                throw new IllegalStateException(
                                        "No valid charge scenarios for squadCasualties=" + squadCasualties
                                                + ", enemyCasualties=" + enemyCasualties
                                                + ", expectedSuccess=" + expectedSuccess
                                );
                            }
                            int enemyIndex = Arrays.asList(SAMPLED_ENEMY_CASUALTIES).indexOf(enemyCasualties);
                            ChargeAttackScenario scenario = validScenarios[(squadCasualties + enemyIndex) % validScenarios.length];
                            Integer[] overflows = new Integer[squadCasualties];

                            for (int i = 0; i < squadCasualties; i++) {
                                if (i < scenario.crits()) { 
                                    overflows[i] = 10;
                                } else if (i < scenario.successfulAttacks()) {
                                    overflows[i] = 0;
                                } else {
                                    overflows[i] = -1;
                                }
                            }

                            return Arguments.of(
                                    squadCasualties,
                                    scenario.difficultyClass(),
                                    enemyCasualties,
                                    scenario.successfulAttacks(),
                                    squadCasualties - scenario.successfulAttacks(),
                                    overflows,
                                    scenario.crits()
                            );
                        }));
    }

    private static Stream<Arguments> failedChargeAttackCases() {
        return sampledChargeAttackCases(FAILED_CHARGE_SCENARIOS, false);
    }

    private static Stream<Arguments> successfulChargeAttackCases() {
        return sampledChargeAttackCases(SUCCESSFUL_CHARGE_SCENARIOS, true);
    }

    @Test
    public void expendedSkirmishAttackCannotBeRolled() {
        HashMap<String, SkirmishAttack> weaponModes = new HashMap<>();
        weaponModes.put("Javelin Throw", new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", weaponModes));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), createSpearsMeleeWeapons());

        squad.rollSkirmishAttack("Javelins", "Javelin Throw", 100);
        assertThrows(IllegalStateException.class, () -> squad.rollSkirmishAttack("Javelins", "Javelin Throw", 100));
    }

    @ParameterizedTest(name = "dc={0}, overflow={1}")
    @CsvSource({
            "5, 0",
            "10, 1",
            "15, 2",
            "20, 3",
            "25, 4"
    })
    public void skirmishAttackDealsOneDamageWhenOverflowIsLessThanFive(int difficultyClass, int overflow) {
        DiceRoll mockDiceRoll = mock(DiceRoll.class);
        when(mockDiceRoll.roll(difficultyClass, 2)).thenReturn(overflow);

        HashMap<String, SkirmishAttack> weaponModes = new HashMap<>();
        weaponModes.put("Throw", new SkirmishAttack("Throw", mockDiceRoll, true, -1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", weaponModes));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), createSpearsMeleeWeapons());

        int damage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);

        assertEquals(1, damage);
    }

    @ParameterizedTest(name = "dc={0}, firstOverflow={1}, secondOverflow={2} firstDamage={3}, secondDamage={4}")
    @CsvSource({
            "5, -1, 0, 0, 1",
            "10, 0, 5, 1, 2",
            "15, 5, 10, 2, 3",
            "20, 10, 15, 3, 4",
            "25, 15, 20, 4, 5",
            "30, 20, -1, 5, 0"
    })
    public void skirmishAttackCanReturnDifferentResultsForSameDcAcrossMultipleRolls(int difficultyClass, int firstOverflow, int secondOverflow, int firstDamageExpected, int secondDamageExpected) {
        DiceRoll mockDiceRoll = mock(DiceRoll.class);
        when(mockDiceRoll.roll(difficultyClass, 2)).thenReturn(firstOverflow, secondOverflow);

        HashMap<String, SkirmishAttack> weaponModes = new HashMap<>();
        weaponModes.put("Throw", new SkirmishAttack("Throw", mockDiceRoll, true, -1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", weaponModes));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), createSpearsMeleeWeapons());

        int firstDamage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);
        int secondDamage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);

        assertEquals(firstDamageExpected, firstDamage);
        assertEquals(secondDamageExpected, secondDamage);
    }

    @ParameterizedTest(name = "dc={0}, overflow={1}")
    @CsvSource({
            "5, -10",
            "5, -14",
            "5, -20",
            "10, -10",
            "10, -14",
            "10, -20",
            "15, -10",
            "15, -14",
            "15, -20",
            "20, -10",
            "20, -14",
            "20, -20",
            "25, -10",
            "25, -14",
            "25, -20"
    })
    public void skirmishAttackFailsWithNegativeOverflow(int difficultyClass, int overflow) {
        DiceRoll mockDiceRoll = mock(DiceRoll.class);
        when(mockDiceRoll.roll(difficultyClass, 2)).thenReturn(overflow);

        HashMap<String, SkirmishAttack> weaponModes = new HashMap<>();
        weaponModes.put("Throw", new SkirmishAttack("Throw", mockDiceRoll, true, -1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", weaponModes));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), createSpearsMeleeWeapons());

        int damage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);

        assertEquals(0, damage);
    }

    @Test
    public void meleeAttackUsesWeaponAndAttackMode() {
        DiceRoll mockDiceRoll = mock(DiceRoll.class);
        when(mockDiceRoll.roll(12, 2)).thenReturn(5);

        HashMap<String, MeleeAttack> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Spears", new MeleeAttack("Spears", mockDiceRoll));

        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        meleeWeapons.put("Spears", new MeleeWeapon("Spears", meleeAttacks));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, createJavelinSkirmishWeapons(), new DiceRoll(20, 1, 0), meleeWeapons);

        int damage = squad.rollMeleeAttack("Spears", "Spears", 12);

        assertEquals(2, damage);
    }

    @Test
    public void invalidMeleeAttackModeThrows() {
        HashMap<String, MeleeAttack> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Spears", new MeleeAttack("Spears", new DiceRoll(8, 4, 0)));

        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        meleeWeapons.put("Spears", new MeleeWeapon("Spears", meleeAttacks));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, createJavelinSkirmishWeapons(), new DiceRoll(20, 1, 0), meleeWeapons);

        assertThrows(IllegalArgumentException.class, () -> squad.rollMeleeAttack("Spears", "Throw!!", 12));
    }

    @Test
    public void constructorThrowsWhenSkirmishWeaponsAreEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, new HashMap<>(), new DiceRoll(20, 1, 0), createSpearsMeleeWeapons()));
    }

    @Test
    public void constructorThrowsWhenMeleeWeaponsAreEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, createJavelinSkirmishWeapons(), new DiceRoll(20, 1, 0), new HashMap<>()));
    }

    @Test
    public void constructorThrowsWhenSkirmishWeaponHasNoModes() {
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", new HashMap<>()));

        assertThrows(IllegalArgumentException.class,
                () -> new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), createSpearsMeleeWeapons()));
    }

    @Test
    public void constructorThrowsWhenMeleeWeaponHasNoModes() {
        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        meleeWeapons.put("Spears", new MeleeWeapon("Spears", new HashMap<>()));

        assertThrows(IllegalArgumentException.class,
                () -> new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, createJavelinSkirmishWeapons(), new DiceRoll(20, 1, 0), meleeWeapons));
    }

    @ParameterizedTest(name = "failedCharge squadCasualties={0}, dc={1}, enemyCasualties={2}, successfulAttacks={3}, failedAttacks={4}, crits={6}")
    @MethodSource("failedChargeAttackCases")
    public void failedChargeAttackTracksCrits(int casualties, int difficultyClass, int enemyCasualties, int successfulAttacks, int failedAttacks, Integer[] overflows, int expectedCrits) {
        DiceRoll mockChargeAttack = mock(DiceRoll.class);
        when(mockChargeAttack.roll(difficultyClass, 7)).thenReturn(overflows[0], Arrays.copyOfRange(overflows, 1, overflows.length));

        Squad squad = createSquadWithChargeAttack(casualties, mockChargeAttack);

        Squad.ChargeResult result = squad.rollChargeAttack(difficultyClass, enemyCasualties);

        assertFalse(result.chargeSuccessful);
        assertEquals(successfulAttacks, result.successfulAttacks);
        assertEquals(failedAttacks, result.failedAttacks);
        assertEquals(expectedCrits, result.numberOfCrits);
    }

    @ParameterizedTest(name = "successfulCharge squadCasualties={0}, dc={1}, enemyCasualties={2}, successfulAttacks={3}, failedAttacks={4}, crits={6}")
    @MethodSource("successfulChargeAttackCases")
    public void successfulChargeAttackTracksCrits(int casualties, int difficultyClass, int enemyCasualties, int successfulAttacks, int failedAttacks, Integer[] overflows, int expectedCrits) {
        DiceRoll mockChargeAttack = mock(DiceRoll.class);
        when(mockChargeAttack.roll(difficultyClass, 7)).thenReturn(overflows[0], Arrays.copyOfRange(overflows, 1, overflows.length));

        Squad squad = createSquadWithChargeAttack(casualties, mockChargeAttack);

        Squad.ChargeResult result = squad.rollChargeAttack(difficultyClass, enemyCasualties);

        assertTrue(result.chargeSuccessful);
        assertEquals(successfulAttacks, result.successfulAttacks);
        assertEquals(failedAttacks, result.failedAttacks);
        assertEquals(expectedCrits, result.numberOfCrits);
    }
}
