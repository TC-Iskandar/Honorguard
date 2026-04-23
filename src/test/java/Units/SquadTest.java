package Units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SquadTest {
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
}
