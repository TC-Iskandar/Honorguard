package Scripts;

import Units.Dice;
import Units.DiceRoll;
import Units.Squad;

import java.util.HashMap;
import java.util.Scanner;

public class InitializationScript {

    public static Squad userMadeSquad() {
        Scanner in = new Scanner(System.in);
        System.out.println("\n Please enter a name: ");
        String name = in.nextLine();
        System.out.println("Please enter a faction: ");
        String faction = in.nextLine();
        System.out.println("Please enter a morale value: ");
        int morale = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a discipline value: ");
        int discipline = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a casualty value: ");
        int casualties = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter an base Skirmish Defense value: ");
        int skirmishDefense = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter an base Melee Defense value: ");
        int meleeDefense = Integer.parseInt(in.nextLine().toUpperCase());
        System.out.println("Please enter a base Charge Defence value: ");
        int chargeDefence = Integer.parseInt(in.nextLine().toUpperCase());

        HashMap<String, DiceRoll> skirmishAttacks = new HashMap<>();
        DiceRoll skirmishSpears = new DiceRoll(6, 4, 0);
        skirmishAttacks.put("Spears", skirmishSpears);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        DiceRoll spearsMelee = new DiceRoll(8, 4, 0);
        meleeAttacks.put("Spears", spearsMelee);
        return new Squad(name, faction, morale, discipline, casualties, skirmishDefense, meleeDefense, chargeDefence, skirmishAttacks, charge, meleeAttacks);
    }

    public static Squad makeGoblins(){
        HashMap<String, DiceRoll> skirmishAttacks = new HashMap<>();
        DiceRoll skirmishDogslicers = new DiceRoll(4, 4, 4);
        skirmishAttacks.put("Dogslicers", skirmishDogslicers);
        DiceRoll skirmishSlings = new DiceRoll(4, 4, 2);
        skirmishAttacks.put("Slings", skirmishSlings);
        DiceRoll charge = new DiceRoll(20, 1, 1);
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        DiceRoll meleeDogslicers = new DiceRoll(6, 5, 5);
        meleeAttacks.put("Dogslicers", meleeDogslicers);
        return new Squad("Goblin Mob","Goblin", 8, 0, 14, 1, 3, 0, skirmishAttacks, charge, meleeAttacks);
    }

    static Squad makeGarrison() {
        HashMap<String, DiceRoll> skirmishAttacks = new HashMap<>();
        DiceRoll skirmishSpears = new DiceRoll(6, 4, 2);
        skirmishAttacks.put("Spears", skirmishSpears);
        DiceRoll skirmishThrow =  new DiceRoll(6, 6, 3);
        skirmishAttacks.put("Throw!!", skirmishThrow);
        DiceRoll skirmishSidearms = new DiceRoll(4, 4, 2);
        skirmishAttacks.put("Sidearms", skirmishSidearms);
        DiceRoll charge = new DiceRoll(20, 1, 3);
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        DiceRoll meleeSpears = new DiceRoll(8, 4, 2);
        meleeAttacks.put("Spears", meleeSpears);
        DiceRoll meleeSidearms = new DiceRoll(4, 4, 2);
        meleeAttacks.put("Sidearms", meleeSidearms);
        return new Squad("Lorrainean Garrison Spearman", "Val DeLaure", 12, 3, 8, 5, 5, 3, skirmishAttacks, charge, meleeAttacks);
    }
}
