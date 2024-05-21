package Scripts;

import Units.DiceRoll;
import Units.Squad;

import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

public class InitializationScript {

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

    public static Squad csvSquad(String fileLocation) {
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation))) {
            List<String[]> records = reader.readAll();

            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                String name = record[0];
                String faction = record[1];
                String weapon = record[2];
                int skirmishDefence = Integer.parseInt(record[3]);
                int morale = Integer.parseInt(record[6]);
                int casualties = Integer.parseInt(record[7]);
                int discipline = Integer.parseInt(record[8]);
                DiceRoll skirmish = new DiceRoll(4, 5, 0);
                DiceRoll charge = new DiceRoll(20, 1, 5); 
                return new Squad(name, faction, skirmishDefence, discipline, morale, casualties, discipline, skirmish, charge);
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    public static Squad makeGoblins(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad("Goblin Units.Squad","Goblin",1, 0, 5, 6, 4, skirmish, charge);
    }

    static Squad makeGarrison() {
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad("Garrison Spearmen", "Garrison", 3, 2, 10, 8, 8, skirmish, charge);
    }
}