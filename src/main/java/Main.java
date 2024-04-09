import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //TODO Print to Text File https://www.baeldung.com/java-write-console-output-file
    //TODO Create State Manager File
    //TODO Implement Attack Roll Class
    //TODO Implementing Fighting
    //TODO Implement Unit Export
    //TODO Implement Unit Import

    public static void main(String[] args) throws FileNotFoundException {
        File a = new File("A.txt");
        PrintStream o = new PrintStream(a);
        PrintStream console = System.out;
        System.setOut(o);
        Squad garrison = makeGarrison();
        System.out.println(garrison.getCurrentStatus());

        System.out.println("\nRolling Garrison Skirmish Attack!");
        garrison.rollSkirmishAttack();
        System.out.println("\nRolling Garrison Charge Attack!");
        garrison.rollChargeAttack();

        Squad goblins = makeGoblins();
        System.out.println("\n \n"+goblins.getCurrentStatus());

        System.out.println("\nRolling Goblin Skirmish Attack!");
        goblins.rollSkirmishAttack();
        System.out.println("\nRolling Goblin Charge Attack!");
        goblins.rollChargeAttack();

        o.close();
        console.close();
    }

    private static Squad makeGoblins(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return  new Squad(1, 0, 5, 6, "Goblin Squad", 4, skirmish, charge);
    }

    private static Squad makeGarrison(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return  new Squad(3, 2, 10, 8, "Garrison Spearmen", 8, skirmish, charge);
    }
}