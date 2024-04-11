import java.util.Scanner;

public class BasicFightingScript {

    public static Squad promptForGarrision() {
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



    private static Squad makeGoblins() {
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad(1, 0, 5, 6, "Goblin Squad", 4, skirmish, charge);
    }

    private static Squad makeGarrison() {
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return new Squad(3, 2, 10, 8, "Garrison Spearmen", 8, skirmish, charge);
    }
}
