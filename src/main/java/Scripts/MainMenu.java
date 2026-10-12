package Scripts;

import Units.Squad;
import Utils.DualPrintStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
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
            if (readYesNo(in, "\n Would you like to create a Custom Squad? (Y/N)")) {
                if (readYesNo(in, "\n Would you like to import it from a JSON file? (Y/N)")) {
                    customSquad = importSquad(in);
                } else {
                    customSquad = InitializationScript.userMadeSquad(in);
                }
                if (customSquad != null) {
                    System.out.println(customSquad.getCurrentStatus());
                }
            }else{
                System.out.println("User chose not to create a custom squad. Returning to Menu.");
            }
        }else{
            if (readYesNo(in, "\n Would you like to view your custom squad? (Y/N)")) {
                System.out.println(customSquad.getCurrentStatus());
            }else{
                System.out.println("User chose not to view their custom squad. Returning to Menu.");
            }
        }
    }

    /**
     * Asks for the path of a squad JSON file and imports it.
     * @param in Scanner to get input from user
     * @return The imported Squad, or null if it could not be imported.
     */
    static Squad importSquad(Scanner in) {
        System.out.println("\n Please enter the path to the JSON file: ");
        String path = in.nextLine().trim();
        try {
            return SquadJson.load(Path.of(path));
        } catch (NoSuchFileException e) {
            System.out.println("No file found at " + path + ". Returning to Menu.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not import the squad: " + e.getMessage());
            System.out.println("Returning to Menu.");
        }
        return null;
    }

    static boolean readYesNo(Scanner in, String question) {
        while (true) {
            System.out.println(question);
            String response = in.nextLine().trim();
            if (response.equalsIgnoreCase("Y")) {
                return true;
            }
            if (response.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Please enter Y or N.");
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
        if (readYesNo(in, "\n Would you like to create a Lorrainean Garrison Spearman Units Squad? (Y/N)")) {
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
        if (readYesNo(in, "\n Would you like to create a Goblin Mob Squad? (Y/N)")) {
            goblins = InitializationScript.makeGoblins();
            System.out.println("Creating Goblin Units.Squad!");
            System.out.println(goblins.getCurrentStatus());
        }else{
            System.out.println("User chose not to create goblins. Returning to Menu.");
        }
    }
}