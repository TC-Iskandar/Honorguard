package Units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiceRollTest {
    @Test
    public void basicTest(){
        DiceRoll skirmish = new DiceRoll(4, 3, 1);
        int overflow = skirmish.roll(2, 0);
        assertTrue(overflow > -1);
        assertTrue(overflow < 14);

    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void rejectsFewerThanOneDie(int numberOfDice) {
        assertThrows(IllegalArgumentException.class, () -> new DiceRoll(6, numberOfDice, 0),
                "Expected a DiceRoll with " + numberOfDice + " dice to be illegal but was not");
    }
}
