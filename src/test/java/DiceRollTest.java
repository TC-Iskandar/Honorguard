import Scripts.InitializationScript;
import Units.DiceRoll;
import Units.Squad;
import org.junit.Test;

public class DiceRollTest {
    @Test
    public void basicTest(){
        DiceRoll skirmish = new DiceRoll(4, 3, 1);
        skirmish.roll(2, 0);
    }

    @Test
    public void goblinSquadTest(){
        Squad goblinSquad = InitializationScript.makeGoblins();
        goblinSquad.rollSkirmishAttack(3);
    }
}
