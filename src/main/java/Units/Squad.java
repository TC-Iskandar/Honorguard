package Units;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<String, DiceRoll> meleeAttacks;
    private String faction;

    public Squad(String name, String faction, int baseMorale, int baseDiscipline, int baseCasualties, int baseSkirmishDefense, int baseMeleeDefense, int baseChargeDefence, HashMap<String, SkirmishWeapon> skirmishAttacks, DiceRoll charge, HashMap<String, DiceRoll> meleeAttacks) {
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
        this.meleeAttacks = meleeAttacks;
        this.faction= faction;
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

    public class ChargeResult{
        public int numberOfCrits;
        public boolean chargeSuccessful;
        public ChargeResult(int numberOfCrits, boolean isChargeSuccessful){
            this.numberOfCrits = numberOfCrits;
            this.chargeSuccessful = isChargeSuccessful;
        }
    }

    public ChargeResult rollChargeAttack(int difficultyClass, int enemyCasualties){
        boolean chargeSuccesful = false;
        int succeededAttacks = 0;
        int numberOfCrits = 0;
        for(int i =0; i< currentCasualties; i++) {
            int chargeDifference = chargeAttack.roll(difficultyClass, currentMorale+currentDiscipline);
            if(chargeDifference < 0){
                System.out.println("Charge attack "+i+" failed.");
            }else {
                succeededAttacks++;
                if(chargeDifference > 9){
                    numberOfCrits++;
                    System.out.println("Charge attack" +i+" critically succeeded.");
                }else{
                    System.out.println("Charge attack "+ i+ " succeeded.");
                }
            }
        }
        if (succeededAttacks > (currentCasualties/2) || succeededAttacks> enemyCasualties){
            chargeSuccesful = true;
        }
        return new ChargeResult(numberOfCrits, chargeSuccesful);
    }

    public int rollSkirmishAttack(String weaponName, String attackName, int difficultyClass) {
        if (skirmishWeapons.containsKey(weaponName) ){
            SkirmishWeapon weapon = skirmishWeapons.get(weaponName);
            if (weapon.getModes().containsKey(attackName)){
                DiceRoll skirmishAttack = weapon.attacks.get(name).getRoll();
                int overflow = skirmishAttack.roll(difficultyClass, currentDiscipline);
                int damage = 0;
                if (overflow >= 0){
                    damage = overflow/5+1;
                    System.out.println(name + " skirmished against DC of "+ difficultyClass + " and succeeded its attack dealing " + damage+" damage.");
                } else {
                    System.out.println(name + " skirmished against DC of "+ difficultyClass + " and failed its attack.");
                }

                return damage;
            } else {
                throw new IllegalArgumentException("Invalid Skirmish Attack Mode for Skirmish Weapon " +weapon.name);
            }
        }else {
            throw new IllegalArgumentException("Invalid Skirmish Weapon for Squad "+name);
        }
    }
    public int rollMeleeAttack(String name, int difficultyClass) {
        if (meleeAttacks.containsKey(name)){
            DiceRoll meleeAttack = meleeAttacks.get(name);
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
            throw new IllegalArgumentException("Invalid Melee Attack.");
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
        defenseString = defenseString + skirmishDefenceLine.indent(2) + meleeDefenseLine.indent(2) + chargeDefenceLine.indent(2);
        String attackString = "Attacks: \n";
        String skirmishAttackLine = "Skirmish Attacks: \n";
        for (String skirmishAttackName : skirmishWeapons.keySet()){
            String attackLine = skirmishAttackName + ": " + skirmishWeapons.get(skirmishAttackName).toString()+"\n";
            skirmishAttackLine += attackLine.indent(2);
        }
        String meleeAttackLine = "Melee Attacks: \n";
        for (String meleeAttackName : meleeAttacks.keySet()){
            String attackLine = meleeAttackName + ": " + meleeAttacks.get(meleeAttackName).toString()+"\n";
            meleeAttackLine += attackLine.indent(2);
        }
        String chargeAttackLine = "Charge Attack: "+ currentCasualties+ " times "+ chargeAttack.toString()+" + "+(currentMorale+currentDiscipline)+"\n";
        attackString = attackString + skirmishAttackLine.indent(2)+ meleeAttackLine.indent(2) + chargeAttackLine.indent(2);
        return nameLine+factionLine+casualtyLine+moraleLine+disciplineLine+defenseString+attackString;
    }

    public void exportAsCSV() throws IOException {
        File file = new File("OutputFiles/"+name+".csv");
        if(!file.createNewFile()){
            throw new IOException("File Already Exists");
        }
    }
}
