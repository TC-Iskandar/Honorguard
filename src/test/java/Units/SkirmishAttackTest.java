package Units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SkirmishAttackTest {
    @Test
    public void useAttackThrowsWhenAttackIsOverused() {
        SkirmishAttack attack = new SkirmishAttack("Javelin Throw", new DiceRoll(6, 1, 0), false, 1);

        attack.useAttack();

        assertThrows(IndexOutOfBoundsException.class, attack::useAttack);
    }
}
