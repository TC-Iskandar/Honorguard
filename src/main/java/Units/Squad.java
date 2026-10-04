package Units;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Squad {
    private final int baseMorale;
    private final int baseDiscipline;
    private final int baseSkirmishDefense;
    private final int baseMeleeDefense;
    private final int baseChargeDefence;
    private final int baseCasualties;
    private final String name;
    private int currentMorale;
    private int currentDiscipline;
    private int currentCasualties;
    private HashMap<String, SkirmishWeapon> skirmishWeapons;
    private DiceRoll chargeAttack;
    private HashMap<String, MeleeWeapon> meleeWeapons;
    private HashMap<String, Trait> traits;
    private Factions faction;

    public Squad(String name, Factions faction, int baseMorale, int baseDiscipline, int baseCasualties, int baseSkirmishDefense, int baseMeleeDefense, int baseChargeDefence, HashMap<String, SkirmishWeapon> skirmishAttacks, DiceRoll charge, HashMap<String, MeleeWeapon> meleeWeapons) {
        this(name, faction, baseMorale, baseDiscipline, baseCasualties, baseSkirmishDefense, baseMeleeDefense, baseChargeDefence, skirmishAttacks, charge, meleeWeapons, new HashMap<>());
    }

    public Squad(String name, Factions faction, int baseMorale, int baseDiscipline, int baseCasualties, int baseSkirmishDefense, int baseMeleeDefense, int baseChargeDefence, HashMap<String, SkirmishWeapon> skirmishAttacks, DiceRoll charge, HashMap<String, MeleeWeapon> meleeWeapons, HashMap<String, Trait> traits) {
        validateSkirmishWeapons(skirmishAttacks);
        validateMeleeWeapons(meleeWeapons);
        this.baseSkirmishDefense = baseSkirmishDefense;
        this.baseDiscipline = baseDiscipline;
        this.baseMorale = baseMorale;
        this.baseCasualties = baseCasualties;
        this.baseMeleeDefense = baseMeleeDefense;
        this.baseChargeDefence = baseChargeDefence;
        this.name = name;
        this.currentMorale= this.baseMorale;
        this.currentDiscipline = this.baseDiscipline;
        this.currentCasualties = this.baseCasualties;
        this.skirmishWeapons = skirmishAttacks;
        this.chargeAttack = charge;
        this.meleeWeapons = meleeWeapons;
        this.traits = traits;
        this.faction= faction;
    }

    private void validateSkirmishWeapons(HashMap<String, SkirmishWeapon> skirmishWeapons) {
        if (skirmishWeapons == null || skirmishWeapons.isEmpty()) {
            throw new IllegalArgumentException("Squad must have at least one Skirmish Weapon.");
        }
        for (SkirmishWeapon weapon : skirmishWeapons.values()) {
            if (weapon.getModes().isEmpty()) {
                throw new IllegalArgumentException("Skirmish Weapon " + weapon.getName() + " must have at least one mode.");
            }
        }
    }

    private void validateMeleeWeapons(HashMap<String, MeleeWeapon> meleeWeapons) {
        if (meleeWeapons == null || meleeWeapons.isEmpty()) {
            throw new IllegalArgumentException("Squad must have at least one Melee Weapon.");
        }
        for (MeleeWeapon weapon : meleeWeapons.values()) {
            if (weapon.getModes().isEmpty()) {
                throw new IllegalArgumentException("Melee Weapon " + weapon.getName() + " must have at least one mode.");
            }
        }
    }

    public int getBaseChargeDefense() {
        return baseChargeDefence;
    }

    public int getBaseMorale(){
        return baseMorale;
    }

    public int getBaseDiscipline() {
        return baseDiscipline;
    }

    public int getMorale(){ return currentMorale; }

    public int getDiscipline(){ return currentDiscipline; }

    public int getMeleeDefense() { return baseMeleeDefense + currentMorale + currentDiscipline; }

    public int getSkirmishDefence(){
        return baseSkirmishDefense + currentDiscipline;
    }

    public int getChargeDefence(){
        return 10 + baseChargeDefence + currentMorale + currentDiscipline;
    }

    public void addTrait(String traitName, Trait trait) {
        traits.put(traitName, trait);
    }

    public boolean hasTrait(String traitName) {
        return traits.containsKey(traitName);
    }

    public class ChargeResult{
        public int numberOfCrits;
        public int successfulAttacks;
        public int failedAttacks;
        public boolean chargeSuccessful;
        public ChargeResult(int numberOfCrits, int successfulAttacks, int failedAttacks, boolean isChargeSuccessful){
            this.numberOfCrits = numberOfCrits;
            this.successfulAttacks = successfulAttacks;
            this.failedAttacks = failedAttacks;
            this.chargeSuccessful = isChargeSuccessful;
        }
    }

    public ChargeResult rollChargeAttack(int difficultyClass, int enemyCasualties){
        boolean chargeSuccesful = false;
        int succeededAttacks = 0;
        int failedAttacks = 0;
        int numberOfCrits = 0;
        for(int i =0; i< currentCasualties; i++) {
            int chargeDifference = chargeAttack.roll(difficultyClass, currentMorale+currentDiscipline);
            if(chargeDifference < 0){
                failedAttacks++;
                System.out.println("Charge attack "+i+" failed.");
            }else {
                succeededAttacks++;
                if(chargeDifference > 9){
                    numberOfCrits++;
                    System.out.println("Charge attack " +i+" critically succeeded.");
                }else{
                    System.out.println("Charge attack "+ i + " succeeded.");
                }
            }
        }
        if (succeededAttacks * 2 >= currentCasualties || succeededAttacks > enemyCasualties){
            chargeSuccesful = true;
        }
        return new ChargeResult(numberOfCrits, succeededAttacks, failedAttacks, chargeSuccesful);
    }

    public int rollSkirmishAttack(String weaponName, String attackName, int difficultyClass) {
        if (skirmishWeapons.containsKey(weaponName)){
            SkirmishWeapon weapon = skirmishWeapons.get(weaponName);
            if (weapon.getModes().containsKey(attackName)){
                SkirmishAttack attack = weapon.getModes().get(attackName);
                if (attack.isExpended()){
                    throw new IllegalStateException("Skirmish Attack Mode " + attackName + " for Skirmish Weapon " + weapon.getName() + " has been expended.");
                }
                DiceRoll skirmishAttack = attack.getRoll();
                int overflow = skirmishAttack.roll(difficultyClass, currentDiscipline);
                attack.useAttack();
                int damage = 0;
                if (overflow >= 0){
                    damage = overflow/5+1;
                    System.out.println(name + " skirmished against DC of "+ difficultyClass + " and succeeded its attack dealing " + damage+" damage.");
                } else {
                    System.out.println(name + " skirmished against DC of "+ difficultyClass + " and failed its attack.");
                }

                return damage;
            } else {
                throw new IllegalArgumentException("Invalid Skirmish Attack Mode for Skirmish Weapon " +weapon.getName());
            }
        }else {
            throw new IllegalArgumentException("Invalid Skirmish Weapon for Squad "+name);
        }
    }
    public int rollMeleeAttack(String weaponName, String attackName, int difficultyClass) {
        if (meleeWeapons.containsKey(weaponName)){
            MeleeWeapon weapon = meleeWeapons.get(weaponName);
            if (weapon.getModes().containsKey(attackName)){
                MeleeAttack attack = weapon.getModes().get(attackName);
                DiceRoll meleeAttack = attack.getRoll();
                int overflow = meleeAttack.roll(difficultyClass, currentDiscipline);
                int damage = 0;
                if (overflow >= 0){
                    damage = overflow/5+1;
                    System.out.println(name + " made a Melee Attack against DC of "+ difficultyClass + " and succeeded its attack dealing " + damage+" damage.");
                } else {
                    System.out.println(name + " made a Melee Attack against DC of "+ difficultyClass + " and failed its attack.");
                }

                return damage;
            } else {
                throw new IllegalArgumentException("Invalid Melee Attack Mode for Melee Weapon " + weapon.getName());
            }
        } else {
            throw new IllegalArgumentException("Invalid Melee Weapon for Squad "+name);
        }
    }

    public String getCurrentStatus(){
        String nameLine = "Status Report for "+name+"\n";
        String factionLine = "Faction: " + faction + "\n";
        String casualtyLine = "Casualty: "+currentCasualties +"/"+baseCasualties+"\n";
        String moraleLine = "Morale: "+currentMorale +"/"+baseMorale+"\n";
        String disciplineLine = "Discipline: "+currentDiscipline +"/"+baseDiscipline+"\n";
        String defenseString = "Defences: \n";
        String skirmishDefenceLine = "Skirmish Defence: "+getSkirmishDefence()+"\n";
        String meleeDefenseLine = "Melee Defence: "+getMeleeDefense()+"\n";
        String chargeDefenceLine = "Charge Defence: "+ getChargeDefence()+"\n";
        defenseString = defenseString + skirmishDefenceLine.indent(2) + meleeDefenseLine.indent(2) + chargeDefenceLine.indent(2) + "\n";
        String traitString = "Traits: \n";
        for (String traitName : traits.keySet()){
            String traitLine = traits.get(traitName).toString()+"\n";
            traitString += traitLine.indent(2);
        }
        String attackString = "Attacks: \n";
        String skirmishAttackLine = "Skirmish Attacks: \n";
        for (String skirmishAttackName : skirmishWeapons.keySet()){
            String attackLine = skirmishWeapons.get(skirmishAttackName).toString()+"\n";
            skirmishAttackLine += attackLine.indent(2);
        }
        String meleeAttackLine = "Melee Attacks: \n";
        for (String meleeWeaponName : meleeWeapons.keySet()){
            String attackLine = meleeWeapons.get(meleeWeaponName).toString()+"\n";
            meleeAttackLine += attackLine.indent(2);
        }
        String chargeAttackLine = "Charge Attack: "+ currentCasualties+ " times "+ chargeAttack.toString()+" + "+(currentMorale+currentDiscipline)+"\n";
        attackString = attackString + skirmishAttackLine.indent(2)+ meleeAttackLine.indent(2) + chargeAttackLine.indent(2);
        return nameLine+factionLine+casualtyLine+moraleLine+disciplineLine+defenseString+traitString+attackString;
    }

    public void exportAsCSV() throws IOException {
        File file = new File("OutputFiles/"+name+".csv");
        if(!file.createNewFile()){
            throw new IOException("File Already Exists");
        }
    }
}
