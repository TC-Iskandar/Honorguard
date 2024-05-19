import java.util.Scanner;
import java.io.FileReader;
import org.apache.commons.csv.*;
import java.io.Reader;

public class BasicFightingScript {

    public static Squad promptForGarrison() {
        Scanner in = new Scanner(System.in);

        Squad garrison;
        System.out.println("\n Would you like to create a Garrison Spearman Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            garrison = makeGarrison();
            System.out.println("Creating Garrison!");
            System.out.println(garrison.getCurrentStatus());
            return garrison;
        }
        return null;
    }

    public static Squad promptForGoblins() {
        Scanner in = new Scanner(System.in);

        Squad goblins;
        System.out.println("\n Would you like to create a Goblin Squad? (Y/N)");
        String response = in.nextLine().toUpperCase();
        if (response.equals("Y")) {
            goblins = makeGoblins();
            System.out.println("Creating Goblin Squad!");
            System.out.println(goblins.getCurrentStatus());
            return goblins;
        }
        return null;
    }

    public static Squad customMadeSquad() {
        String csvfile = "squad_test.csv";
        try (
            Reader reader = new FileReader(csvfile);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("name");
                String faction = csvRecord.get("faction");
                int armor = Integer.parseInt(csvRecord.get("armor"));
                int discipline = Integer.parseInt(csvRecord.get("discipline"));
                int morale = Integer.parseInt(csvRecord.get("morale"));
                int casualties = Integer.parseInt(csvRecord.get("casualties"));
                int chargeDefence = Integer.parseInt(csvRecord.get("chargeDefence"));
                DiceRoll skirmish = new DiceRoll(6, 4, 0);
                DiceRoll charge = new DiceRoll(20, 1, 7);
                return new Squad(name, faction, armor, discipline, morale, casualties, chargeDefence, skirmish, charge);
            }
        } catch (Exception e) {
            System.out.println("Error reading file");
        }
        return null;
    }

    private static Squad userMadeSquad() {
        Scanner in = new Scanner(System.in);
        System.out.println("\n Please enter a name: ");
        String name = in.nextLine();
        System.out.println("Please enter a faction: ");
        String faction = in.nextLine();
        System.out.println("Please enter an armor value: ");
        int armor = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a discipline value: ");
        int discipline = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a morale value: ");
        int morale = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a casualty value: ");
        int casualties = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a charge defence value: ");
        int chargeDefence = Integer.parseInt(in.nextLine().toUpperCase());

        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad(name, faction, armor, discipline, morale, casualties, chargeDefence, skirmish, charge);
    }

    private static Squad makeGoblins(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad("Goblin Squad","Goblin",1, 0, 5, 6, 4, skirmish, charge);
    }

    private static Squad makeGarrison() {
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad("Garrison Spearmen", "Garrison", 3, 2, 10, 8, 8, skirmish, charge);
    }
}
