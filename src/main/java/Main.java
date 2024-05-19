import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //TODO Create State Manager File - Partially Done
    //TODO Implement Attack Roll Class - Partially done
    //TODO Implementing Fighting
    //TODO Implement Unit Export
    //TODO Implement Unit Import

    public static void main(String[] args) throws FileNotFoundException {
        DualPrintStream dualPrintStream = setupPrintStreams();
        boolean exit = false;
        Scanner in = new Scanner(System.in);
        while (!exit){
            System.out.println("\n Choose an option: "
            +"\n 1. Create a unit of Garrison Spearman"
            +"\n 2. Create a unit of Goblins"
            +"\n 3. Exit Program");
            String response = in.nextLine();
            switch (response){
                case "1":
                    createGarrison();
                    break;
                case "2":
                    createGoblins();
                    break;
                case "3": 
                    createUserCreatedSquad();
                    break;
                case "4":
                    System.out.println("Exiting Program");
                    dualPrintStream.close();
                    return;
                default:
                    System.out.println("User did not choose a valid menu option. Returning to main menu.");
            }
        }
    }

    private static DualPrintStream setupPrintStreams() throws FileNotFoundException {
        File a = new File("src/main/java/OutputFiles/Output.txt");
        PrintStream printToFile = new PrintStream(a);
        PrintStream console = System.out;
        DualPrintStream dualOut = new DualPrintStream(printToFile, console);
        System.setOut(dualOut);
        return dualOut;
    }


    private static void createUserCreatedSquad() {
        Squad userSquad = BasicFightingScript.customMadeSquad();
        if (userSquad == null){
            System.out.println("User chose not to create a custom squad. Returning to Menu.");
            return;
        }
        System.out.println("\n \n" + userSquad.getCurrentStatus());

        System.out.println("\nRolling User Skirmish Attack!");
        userSquad.rollSkirmishAttack();
        System.out.println("\nRolling User Charge Attack!");
        userSquad.rollChargeAttack();
    }

    private static void createGarrison(){
        Squad garrison = BasicFightingScript.promptForGarrison();

        if (garrison == null){
            System.out.println("User chose not to create a garrison. Returning to Menu.");
            return;
        }

        System.out.println("\nRolling Garrison Skirmish Attack!");
        garrison.rollSkirmishAttack();
        System.out.println("\nRolling Garrison Charge Attack!");
        garrison.rollChargeAttack();

    }

    private static void createGoblins(){
        Squad goblins =  BasicFightingScript.promptForGoblins();

        if (goblins == null){
            System.out.println("User chose not to create a garrison. Returning to Menu.");
            return;
        }
        System.out.println("\n \n" + goblins.getCurrentStatus());

        System.out.println("\nRolling Goblin Skirmish Attack!");
        goblins.rollSkirmishAttack();
        System.out.println("\nRolling Goblin Charge Attack!");
        goblins.rollChargeAttack();

    }

}