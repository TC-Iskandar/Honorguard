package Units;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MeleeWeaponTest {
    @Test
    public void meleeAttackStoresMinimumAndMaximumRange() {
        MeleeAttack attack = new MeleeAttack("Spears", new DiceRoll(8, 4, 2), 2, 10);

        assertEquals(2, attack.getMinRange());
        assertEquals(10, attack.getMaxRange());
    }

    @Test
    public void meleeAttackRejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new MeleeAttack("Spears", new DiceRoll(8, 4, 2), -1, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new MeleeAttack("Spears", new DiceRoll(8, 4, 2), 11, 10));
    }

    @Test
    public void multipleModeTest() {
        String output =
                "Name: Pikes\n" +
                "Modes: 2\n" +
                "  Pikes 10-25ft: 3d6 + 0 [10-25 ft]\n" +
                "  Pikes 25-40ft: 5d6 + 0 [25-40 ft]\n";
        HashMap<String, MeleeAttack> attacks = new LinkedHashMap<>();
        attacks.put("Pikes 10-25ft", new MeleeAttack("Pikes 10-25ft", new DiceRoll(6, 3, 0), 10, 25));
        attacks.put("Pikes 25-40ft", new MeleeAttack("Pikes 25-40ft", new DiceRoll(6, 5, 0), 25, 40));
        MeleeWeapon pikes = new MeleeWeapon("Pikes", attacks);

        assertEquals(output, pikes.toString());
    }
}
