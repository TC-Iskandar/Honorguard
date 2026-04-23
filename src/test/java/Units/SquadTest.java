package Units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SquadTest {
    private record ChargeAttackScenario(int difficultyClass, int successfulAttacks, int crits) {}

    private static Squad createSquadWithChargeAttack(int casualties, DiceRoll chargeAttack) {
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Dagger", new DiceRoll(4, 1, 0));
        return new Squad("Test Squad", "Test Faction", 5, 2, casualties, 1, 1, 1, skirmishWeapons, chargeAttack, meleeAttacks);
    }

    private static Stream<Arguments> chargeAttackCases() {
        ChargeAttackScenario[] scenarios = {
                new ChargeAttackScenario(5, 0, 0),
                new ChargeAttackScenario(10, 1, 0),
                new ChargeAttackScenario(15, 1, 1),
                new ChargeAttackScenario(20, 2, 1),
                new ChargeAttackScenario(25, 3, 2),
                new ChargeAttackScenario(30, 4, 3)
        };

        Integer[] sampledEnemyCasualties = {1, 5, 10};

        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .flatMap(squadCasualties -> Stream.of(sampledEnemyCasualties)
                        .map(enemyCasualties -> {
                            ChargeAttackScenario[] validScenarios = Arrays.stream(scenarios)
                                    .filter(scenario -> scenario.successfulAttacks() <= squadCasualties)
                                    .filter(scenario -> scenario.crits() <= scenario.successfulAttacks())
                                    .toArray(ChargeAttackScenario[]::new);

                            // We intentionally sample one scenario per (squadCasualties, enemyCasualties) pair
                            // instead of running every valid scenario combination. That keeps this test set at
                            // 10 squad sizes x 3 enemy casualty samples = 30 cases while still covering the full
                            // squad casualty range, low/mid/high enemy casualty counts, and a rotating mix of
                            // DC/success/crit patterns. The modulo makes the selection deterministic, so the same
                            // inputs always pick the same scenario and the suite remains stable across runs.
                            int enemyIndex = Arrays.asList(sampledEnemyCasualties).indexOf(enemyCasualties);
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

                            boolean expectedSuccess = scenario.successfulAttacks() > (squadCasualties / 2) || scenario.successfulAttacks() > enemyCasualties;
                            return Arguments.of(
                                    squadCasualties,
                                    scenario.difficultyClass(),
                                    enemyCasualties,
                                    scenario.successfulAttacks(),
                                    overflows,
                                    expectedSuccess,
                                    scenario.crits()
                            );
                        }));
    }

    @Test
    public void expendedSkirmishAttackCannotBeRolled() {
        HashMap<String, SkirmishAttack> weaponModes = new HashMap<>();
        weaponModes.put("Javelin Throw", new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1));

        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        skirmishWeapons.put("Javelins", new SkirmishWeapon("Javelins", weaponModes));

        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Dagger", new DiceRoll(4, 1, 0));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), meleeAttacks);

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

        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Dagger", new DiceRoll(4, 1, 0));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), meleeAttacks);

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

        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        meleeAttacks.put("Dagger", new DiceRoll(4, 1, 0));

        Squad squad = new Squad("Test Squad", "Test Faction", 5, 2, 3, 1, 1, 1, skirmishWeapons, new DiceRoll(20, 1, 0), meleeAttacks);

        int firstDamage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);
        int secondDamage = squad.rollSkirmishAttack("Javelins", "Throw", difficultyClass);

        assertEquals(firstDamageExpected, firstDamage);
        assertEquals(secondDamageExpected, secondDamage);
    }

    @ParameterizedTest(name = "squadCasualties={0}, dc={1}, enemyCasualties={2}, successfulAttacks={3}, success={5}, crits={6}")
    @MethodSource("chargeAttackCases")
    public void chargeAttackTracksSuccessAndCrits(int casualties, int difficultyClass, int enemyCasualties, int successfulAttacks, Integer[] overflows, boolean expectedSuccess, int expectedCrits) {
        DiceRoll mockChargeAttack = mock(DiceRoll.class);
        when(mockChargeAttack.roll(difficultyClass, 7)).thenReturn(overflows[0], Arrays.copyOfRange(overflows, 1, overflows.length));

        Squad squad = createSquadWithChargeAttack(casualties, mockChargeAttack);

        Squad.ChargeResult result = squad.rollChargeAttack(difficultyClass, enemyCasualties);

        assertEquals(expectedSuccess, result.chargeSuccessful);
        assertEquals(expectedCrits, result.numberOfCrits);
    }
}
