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

    private static void createGarrison(){
        Scanner in = new Scanner(System.in);

        Squad garrison;
        System.out.println("\n Would you like to create a Garrison Spearman Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            garrison = BasicFightingScript.makeGarrison();
            System.out.println("Creating Garrison!");
            System.out.println(garrison.getCurrentStatus());
        }else{
            System.out.println("User chose not to create a garrison. Returning to Menu.");
        }

    }

    private static void createGoblins(){
        Scanner in = new Scanner(System.in);

        Squad goblins;
        System.out.println("\n Would you like to create a Goblin Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            goblins = BasicFightingScript.makeGoblins();
            System.out.println("Creating Goblin Squad!");
            System.out.println(goblins.getCurrentStatus());
        }else{
            System.out.println("User chose not to create a garrison. Returning to Menu.");
        }
    }

}