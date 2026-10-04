package Scripts;

import Units.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class InitializationScript {
    /***
     * Prompts the user for necessary arguments to make a squad.
     * @param in Scanner to get input from user
     * @return A completed usermade Squad.
     */
    public static Squad userMadeSquad(Scanner in) {
        System.out.println("\n Please enter a name: ");
        String name = in.nextLine();
        System.out.println("Please enter a faction: ");
        String faction = in.nextLine();
        int morale = readInt(in, "Please enter a morale value: ");
        int discipline = readInt(in, "Please enter a discipline value: ");
        int casualties = readInt(in, "Please enter a casualty value: ");
        int skirmishDefense = readInt(in, "Please enter an base Skirmish Defense value: ");
        int meleeDefense = readInt(in, "Please enter an base Melee Defense value: ");
        int chargeDefence = readInt(in, "Please enter a base Charge Defence value: ");
        int baseChargeAttack = readInt(in, "Please enter a base Charge Attack value: ");
        DiceRoll charge = new DiceRoll(20, 1, baseChargeAttack);
        System.out.println("Adding Skirmish Attacks: ");
        HashMap<String, SkirmishWeapon> skirmishWeapons = getCustomSkirmishWeapons(in);
        System.out.println("Adding Melee Attacks: ");
        HashMap<String, MeleeWeapon> meleeWeapons = getCustomMeleeWeapons(in);
        return new Squad(name, faction, morale, discipline, casualties, skirmishDefense, meleeDefense, chargeDefence, skirmishWeapons, charge, meleeWeapons);
    }

    /**
     * Takes input from the user to get Skirmish Weapons with modes and SkirmishAttacks for the customSquad
     * @param in Scanner to get input from user
     * @return A HashMap of SkirmishWeapon.name and SkirmishWeapons containing a HashMap of SkirmishAttacks complete with
     * name, DiceRoll, isInfinite, and number of uses.
     */
    private static HashMap<String, SkirmishWeapon> getCustomSkirmishWeapons(Scanner in){
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        int numberOfSkirmishWeapons = readInt(in, "How many Skirmish Weapons would you like to add?", 1);
        for (int i = 0; i < numberOfSkirmishWeapons; i++) {
            System.out.println("Adding Skirmish Weapon number "+(i+1));
            String weaponName = readNewName(in, "What is the name of this Skirmish Weapon?", skirmishWeapons.keySet());
            HashMap<String, SkirmishAttack> skirmishAttacks = new HashMap<>();
            int modes = readInt(in, "How many modes does this Skirmish Weapon have?", 1);
            for (int j = 0; j < modes; j++) {
                System.out.println("Adding mode number " + (j + 1));
                String attackName = readNewName(in, "What is the mode name?", skirmishAttacks.keySet());
                int dieSize = readDieSize(in);
                int numDice = readInt(in, "What is the number of dice?");
                int modifier = readInt(in, "What is base modifier");
                boolean infinite = readBoolean(in, "Is it infinite (true/false)?");
                int numberOfUses = -1;
                if (!infinite) {
                    numberOfUses = readInt(in, "What is the number of uses?", 1);
                }
                SkirmishAttack attack = new SkirmishAttack(attackName, new DiceRoll(dieSize, numDice, modifier), infinite, numberOfUses);
                skirmishAttacks.put(attackName, attack);
            }
            skirmishWeapons.put(weaponName, new SkirmishWeapon(weaponName, skirmishAttacks));
        }
        return skirmishWeapons;
    }

    /**
     *
     * @param in Scanner to get User Input
     * @return A HashMap of MeleeWeapon.name and MeleeWeapons containing a HashMap of MeleeAttacks.
     */
    private static HashMap<String, MeleeWeapon> getCustomMeleeWeapons(Scanner in) {
        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        int numberOfMeleeWeapons = readInt(in, "How many Melee Weapons would you like to add?", 1);
        for (int i = 0; i < numberOfMeleeWeapons; i++) {
            System.out.println("Adding Melee Weapon number "+(i+1));
            String weaponName = readNewName(in, "What is the name of this Melee Weapon?", meleeWeapons.keySet());
            HashMap<String, MeleeAttack> meleeAttacks = new HashMap<>();
            int modes = readInt(in, "How many modes does this Melee Weapon have?", 1);
            for (int j = 0; j < modes; j++) {
                System.out.println("Adding mode number " + (j + 1));
                String attackName = readNewName(in, "What is the attack name?", meleeAttacks.keySet());
                int dieSize = readDieSize(in);
                int numDice = readInt(in, "What is the number of dice?");
                int modifier = readInt(in, "What is base modifier");
                MeleeAttack newMeleeAttack = new MeleeAttack(attackName, new DiceRoll(dieSize, numDice, modifier));
                meleeAttacks.put(attackName, newMeleeAttack);
            }
            meleeWeapons.put(weaponName, new MeleeWeapon(weaponName, meleeAttacks));
        }
        return meleeWeapons;
    }

    private static int readInt(Scanner in, String question) {
        return readInt(in, question, Integer.MIN_VALUE);
    }

    private static int readInt(Scanner in, String question, int min) {
        while (true) {
            System.out.println(question);
            String input = in.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min) {
                    return value;
                }
                System.out.println("Must be at least " + min + ". Try again.");
            } catch (NumberFormatException e) {
                System.out.println("\"" + input + "\" is not a valid whole number. Try again.");
            }
        }
    }

    /**
     * Asks for a die size until it is one of Dice.validDiceSizes.
     */
    private static int readDieSize(Scanner in) {
        while (true) {
            int dieSize = readInt(in, "What is the die size?");
            if (Arrays.stream(Dice.validDiceSizes).anyMatch(size -> size == dieSize)) {
                return dieSize;
            }
            System.out.println(dieSize + " is not a valid die size. Accepted die sizes are: " + Arrays.toString(Dice.validDiceSizes));
        }
    }

    /**
     * Asks the question until the answer is true or false.
     */
    private static boolean readBoolean(Scanner in, String question) {
        while (true) {
            System.out.println(question);
            String input = in.nextLine().trim();
            if (input.equalsIgnoreCase("true")) {
                return true;
            }
            if (input.equalsIgnoreCase("false")) {
                return false;
            }
            System.out.println("Please enter true or false.");
        }
    }

    /**
     * Asks the question until the answer is a name that is not already in takenNames.
     */
    private static String readNewName(Scanner in, String question, Set<String> takenNames) {
        while (true) {
            System.out.println(question);
            String name = in.nextLine().trim();
            if (!takenNames.contains(name)) {
                return name;
            }
            System.out.println("\"" + name + "\" is already used. Choose a different name.");
        }
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
        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        HashMap<String, MeleeAttack> dogslicerMeleeModes = new HashMap<>();
        dogslicerMeleeModes.put("Dogslicers", new MeleeAttack("Dogslicers", new DiceRoll(6, 5, 5)));
        meleeWeapons.put("Dogslicers", new MeleeWeapon("Dogslicers", dogslicerMeleeModes));
        return new Squad("Goblin Mob","Goblin", 8, 0, 14, 1, 3, 0, skirmishWeapons, charge, meleeWeapons);
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
        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        HashMap<String, MeleeAttack> spearMeleeModes = new HashMap<>();
        spearMeleeModes.put("Spears", new MeleeAttack("Spears", new DiceRoll(8, 4, 2)));
        meleeWeapons.put("Spears", new MeleeWeapon("Spears", spearMeleeModes));
        HashMap<String, MeleeAttack> sidearmMeleeModes = new HashMap<>();
        sidearmMeleeModes.put("Sidearms", new MeleeAttack("Sidearms", new DiceRoll(4, 4, 2)));
        meleeWeapons.put("Sidearms", new MeleeWeapon("Sidearms", sidearmMeleeModes));
        return new Squad("Lorrainean Garrison Spearman", "Val DeLaure", 12, 3, 8, 5, 5, 3, skirmishWeapons, charge, meleeWeapons);
    }
}
