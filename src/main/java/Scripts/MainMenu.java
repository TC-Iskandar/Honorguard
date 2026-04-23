package Scripts;

import Units.Squad;
import Utils.DualPrintStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class MainMenu {
    private static Squad customSquad = null;

    public static void menuLoop() throws FileNotFoundException {
        DualPrintStream dualPrintStream = setupPrintStreams();
        boolean exit = false;
        Scanner in = new Scanner(System.in);
        while (!exit){
            System.out.println("\n Choose an option: "
                    +"\n 1. Create a unit of Garrison Spearman"
                    +"\n 2. Create a unit of Goblins"
                    +"\n 3. Create or view a Custom Unit"
                    +"\n 4. Exit Program");
            String response = in.nextLine();
            switch (response){
                case "1":
                    createGarrison();
                    break;
                case "2":
                    createGoblins();
                    break;
                case "3":
                    createOrViewCustomSquad();
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

    private static void createOrViewCustomSquad() {
        Scanner in = new Scanner(System.in);
        if(customSquad == null){
            System.out.println("\n Would you like to create a Custom Squad? (Y/N)");
            String response = in.nextLine().toUpperCase();
            if (response.equals("Y")) {
                customSquad = InitializationScript.userMadeSquad();
                System.out.println(customSquad.getCurrentStatus());
            }else{
                System.out.println("User chose not to create a custom squad. Returning to Menu.");
            }
        }else{
            System.out.println("\n Would you like to view your custom squad? (Y/N)");
            String response = in.nextLine().toUpperCase();
            if (response.equals("Y")) {
                System.out.println(customSquad.getCurrentStatus());
            }else{
                System.out.println("User chose not to view their custom squad. Returning to Menu.");
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
        System.out.println("\n Would you like to create a Lorrainean Garrison Spearman Units Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            garrison = InitializationScript.makeGarrison();
            System.out.println("Creating Garrison!");
            System.out.println(garrison.getCurrentStatus());
        }else{
            System.out.println("User chose not to create a garrison. Returning to Menu.");
        }

    }

    private static void createGoblins(){
        Scanner in = new Scanner(System.in);

        Squad goblins;
        System.out.println("\n Would you like to create a Goblin Mob Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            goblins = InitializationScript.makeGoblins();
            System.out.println("Creating Goblin Units.Squad!");
            System.out.println(goblins.getCurrentStatus());
        }else{
            System.out.println("User chose not to create a garrison. Returning to Menu.");
        }
    }
}