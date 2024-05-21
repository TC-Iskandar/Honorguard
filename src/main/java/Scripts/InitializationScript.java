package Scripts;

import Units.DiceRoll;
import Units.Squad;

import java.util.Scanner;
import java.io.FileReader;
import org.apache.commons.csv.*;
import java.io.Reader;

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
