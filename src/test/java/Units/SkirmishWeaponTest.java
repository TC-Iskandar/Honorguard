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
                "  Pikes 10-25ft: 3d6 + 0 [10-25 ft] \n" +
                "  Pikes 25-40ft: 5d6 + 0 [25-40 ft] \n";
        HashMap<String, SkirmishAttack> attacks = new HashMap<>();
        attacks.put("Pikes 10-25ft", new SkirmishAttack("Pikes 10-25ft", new DiceRoll(6, 3, 0), true, -1, 10, 25));
        attacks.put("Pikes 25-40ft", new SkirmishAttack("Pikes 25-40ft", new DiceRoll(6, 5, 0), true, -1, 25, 40));
        SkirmishWeapon pikes = new SkirmishWeapon("Pikes", attacks);
        assertEquals(pikes.toString(), output);

    }
}
