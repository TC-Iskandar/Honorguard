package Units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiceRollTest {
    @Test
    public void basicTest(){
        DiceRoll skirmish = new DiceRoll(4, 3, 1);
        int overflow = skirmish.roll(2, 0);
        assertTrue(overflow > -1);
        assertTrue(overflow < 14);

    }
}
