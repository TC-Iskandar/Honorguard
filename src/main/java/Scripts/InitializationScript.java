package Scripts;

import Units.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        HashMap<String, SkirmishWeapon> skirmishWeapons = getCustomSkirmishWeapons(in);
        System.out.println("Adding Melee Attacks: ");
        HashMap<String, DiceRoll> meleeAttacks = getCustomDiceRolls(in);
        return new Squad(name, faction, morale, discipline, casualties, skirmishDefense, meleeDefense, chargeDefence, skirmishWeapons, charge, meleeAttacks);
    }

    private static HashMap<String, SkirmishWeapon> getCustomSkirmishWeapons(Scanner in){
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        System.out.println("How many Skirmish Weapons would you like to add?");
        int numberOfSkirmishWeapons = Integer.parseInt(in.nextLine().toUpperCase());
        for (int i = 0; i < numberOfSkirmishWeapons; i++) {
            System.out.println("Adding Skirmish Weapon number "+(i+1));
            System.out.println("What is the name of this Skirmish Weapon?");
            String weaponName = in.nextLine();
            HashMap<String, SkirmishAttack> skirmishAttacks = new HashMap<>();
            System.out.println("How many modes does this Skirmish Weapon have?");
            int modes = Integer.parseInt(in.nextLine().toUpperCase());
            for (int j = 0; j < modes; j++) {
                System.out.println("Adding mode number " + (j + 1));
                boolean validDice = false;
                while (!validDice) {
                    System.out.println("What is the mode name?");
                    String attackName = in.nextLine();
                    System.out.println("What is the die size?");
                    int dieSize = Integer.parseInt(in.nextLine().toUpperCase());
                    System.out.println("What is the number of dice?");
                    int numDice = Integer.parseInt(in.nextLine().toUpperCase());
                    System.out.println("What is base modifier");
                    int modifier = Integer.parseInt(in.nextLine().toUpperCase());
                    System.out.println("Is it infinite (true/false)?");
                    Boolean infinite = Boolean.parseBoolean(in.nextLine().toUpperCase());
                    int numberOfUses = -1;
                    if (!infinite) {
                        System.out.println("What is the number of uses?");
                        numberOfUses = Integer.parseInt(in.nextLine().toUpperCase());
                    }
                    validDice = true;
                    try {
                        SkirmishAttack attack = new SkirmishAttack(attackName, new DiceRoll(dieSize, numDice, modifier), infinite, numberOfUses);
                        skirmishAttacks.put(attackName, attack);
                    } catch (IllegalArgumentException e) {
                        validDice = false;
                        System.out.println("Invalid die size, try again. Accepted die size are: ");
                        for (int validDieSize : Dice.validDiceSizes) {
                            System.out.println("  " + validDieSize);
                        }
                    }

                }
            }
            skirmishWeapons.put(weaponName, new SkirmishWeapon(weaponName, skirmishAttacks));
        }
        return skirmishWeapons;
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
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        HashMap<String, SkirmishAttack> dogslicerModes = new HashMap<>();
        SkirmishAttack DogslicerAttack = new SkirmishAttack("Dogslicers", new DiceRoll(4, 4, 2), true, -1);
        dogslicerModes.put("Dogslicers", DogslicerAttack);
        skirmishWeapons.put("Dogslicers", new SkirmishWeapon("Dogslicers", dogslicerModes));
        HashMap<String, SkirmishAttack> slingsModes = new HashMap<>();
        SkirmishAttack slingsAttack = new SkirmishAttack("Slings", new DiceRoll(4, 4, 2),  false, 15);
        slingsModes.put("Slings", slingsAttack);
        skirmishWeapons.put("Slings", new SkirmishWeapon("Slings", slingsModes));
        DiceRoll charge = new DiceRoll(20, 1, 1);
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        DiceRoll meleeDogslicers = new DiceRoll(6, 5, 5);
        meleeAttacks.put("Dogslicers", meleeDogslicers);
        return new Squad("Goblin Mob","Goblin", 8, 0, 14, 1, 3, 0, skirmishWeapons, charge, meleeAttacks);
    }

    static Squad makeGarrison() {
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        HashMap<String, SkirmishAttack> spearModes = new HashMap<>();
        SkirmishAttack spearThrow = new SkirmishAttack("Throw!!",  new DiceRoll(6, 6, 3), false, 1);
        SkirmishAttack spearNormal = new SkirmishAttack("Spears", new DiceRoll(6, 4, 2), true, -1);
        spearModes.put("Spears", spearNormal);
        spearModes.put("Throw!!", spearThrow);
        skirmishWeapons.put("Spears", new SkirmishWeapon("Spears", spearModes));
        HashMap<String, SkirmishAttack> sidearmsModes = new HashMap<>();
        SkirmishAttack sidearmsNormal = new SkirmishAttack("Sidearms", new DiceRoll(4, 4, 2), true, -1);
        sidearmsModes.put("Sidearms", sidearmsNormal);
        skirmishWeapons.put("Sidearms", new SkirmishWeapon("Sidearms", sidearmsModes));
        DiceRoll charge = new DiceRoll(20, 1, 3);
        HashMap<String, DiceRoll> meleeAttacks = new HashMap<>();
        DiceRoll meleeSpears = new DiceRoll(8, 4, 2);
        meleeAttacks.put("Spears", meleeSpears);
        DiceRoll meleeSidearms = new DiceRoll(4, 4, 2);
        meleeAttacks.put("Sidearms", meleeSidearms);
        return new Squad("Lorrainean Garrison Spearman", "Val DeLaure", 12, 3, 8, 5, 5, 3, skirmishWeapons, charge, meleeAttacks);
    }
}
