package Units;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkirmishWeaponTest {
    @Test
    public void multipleRangeTest(){
        String output =
                "Name: Pikes\n" +
                "Modes: 2\n" +
                "  Pikes 10-25ft: 3d6 + 0 \n" +
                "  Pikes 25-40ft: 5d6 + 0 \n";
        HashMap<String, SkirmishAttack> attacks = new HashMap<>();
        attacks.put("Pikes 10-25ft", new SkirmishAttack("Pikes 10-25ft", new DiceRoll(6, 3, 0), true, -1));
        attacks.put("Pikes 25-40ft", new SkirmishAttack("Pikes 25-40ft", new DiceRoll(6, 5, 0), true, -1));
        SkirmishWeapon pikes = new SkirmishWeapon("Pikes", attacks);
        assertEquals(pikes.toString(), output);

    }
}
