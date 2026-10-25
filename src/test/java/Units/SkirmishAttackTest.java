package Units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkirmishAttackTest {
    @Test
    public void storesMinimumAndMaximumRange() {
        SkirmishAttack attack = new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1, 5, 25);

        assertEquals(5, attack.getMinRange());
        assertEquals(25, attack.getMaxRange());
    }

    @Test
    public void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1, -1, 25));
        assertThrows(IllegalArgumentException.class,
                () -> new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1, 26, 25));
    }

    @Test
    public void useAttackThrowsWhenAttackIsOverused() {
        SkirmishAttack attack = new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1, 0, 25);

        attack.useAttack();

        assertThrows(IndexOutOfBoundsException.class, attack::useAttack);
    }
}
