import java.util.Scanner;

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
