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
        System.out.println("Please enter a base Charge Attack value: ");
        int baseChargeAttack = Integer.parseInt(in.nextLine().toUpperCase());
        DiceRoll charge = new DiceRoll(20, 1, baseChargeAttack);
        System.out.println("Adding Skirmish Attacks: ");
        HashMap<String, DiceRoll> skirmishAttacks = getCustomDiceRolls(in);
        System.out.println("Adding Melee Attacks: ");
        HashMap<String, DiceRoll> meleeAttacks = getCustomDiceRolls(in);
        return new Squad(name, faction, morale, discipline, casualties, skirmishDefense, meleeDefense, chargeDefence, skirmishAttacks, charge, meleeAttacks);
    }

    private static HashMap<String, DiceRoll> getCustomDiceRolls(Scanner in) {
        HashMap<String, DiceRoll> skirmishAttacks = new HashMap<>();
        System.out.println("How many Attacks would you like to add?");
        int numberOfSkirmishAttacks = Integer.parseInt(in.nextLine().toUpperCase());
        for (int i = 0; i < numberOfSkirmishAttacks; i++) {
            System.out.println("Adding attack number "+(i+1));
            boolean validDice = false;
            while (!validDice){
                System.out.println("What is the attack name?");
                String attackName = in.nextLine();
                System.out.println("What is the die size?");
                int dieSize =  Integer.parseInt(in.nextLine().toUpperCase());
                System.out.println("What is the number of dice?");
                int numDice =  Integer.parseInt(in.nextLine().toUpperCase());
                System.out.println("What is base modifier");
                int modifier = Integer.parseInt(in.nextLine().toUpperCase());
                validDice = true;
                try{
                    DiceRoll newSkirmishAttack = new DiceRoll(dieSize, numDice, modifier);
                    skirmishAttacks.put(attackName, newSkirmishAttack);
                }catch (IllegalArgumentException e){
                    validDice = false;
                    System.out.println("Invalid die size, try again. Accepted die size are: ");
                    for(int validDieSize : Dice.validDiceSizes){
                        System.out.println("  "+validDieSize);
                    }
                }
            }

        }
        return skirmishAttacks;
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
