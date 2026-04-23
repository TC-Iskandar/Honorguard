package Units;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertThrows;

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
}
