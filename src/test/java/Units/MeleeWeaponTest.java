package Units;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeleeWeaponTest {
    @Test
    public void multipleModeTest() {
        String output =
                "Name: Pikes\n" +
                "Modes: 2\n" +
                "  Pikes 10-25ft: 3d6 + 0\n" +
                "  Pikes 25-40ft: 5d6 + 0\n";
        HashMap<String, MeleeAttack> attacks = new LinkedHashMap<>();
        attacks.put("Hook", new MeleeAttack("Pikes 10-25ft", new DiceRoll(6, 3, 0)));
        attacks.put("Chop", new MeleeAttack("Pikes 25-40ft", new DiceRoll(6, 5, 0)));
        MeleeWeapon poleaxe = new MeleeWeapon("Pikes", attacks);

        assertEquals(output, poleaxe.toString());
    }
}
