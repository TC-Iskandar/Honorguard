import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertThrows;

public class DiceTest {

    //TODO refactor test to include larger range of invalid numbers.
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -3, 15, 110})
    public void InvalidDiceSize(int size){
        assertThrows("Expected Dice of Size " + size+" to be illegal but was not",
                IllegalArgumentException.class,
                ()->{
                    Dice dice= new Dice(size);
                    int x = dice.roll(3);
                });
    }

}

