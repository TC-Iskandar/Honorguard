import Scripts.InitializationScript;
import Units.DiceRoll;
import Units.Squad;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DiceRollTest {
    @Test
    public void basicTest(){
        DiceRoll skirmish = new DiceRoll(4, 3, 1);
        int overflow = skirmish.roll(2, 0);
        assertTrue(overflow > -1);
        assertTrue(overflow < 14);

    }

    @Test
    public void goblinSquadTest(){
        Squad goblinSquad = InitializationScript.makeGoblins();
        int damage = goblinSquad.rollSkirmishAttack(3);
    }
}
