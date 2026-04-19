package Units;

import java.io.File;
import java.io.IOException;

public class Squad {
    private final int baseMorale;
    private final int baseDiscipline;
    private final int baseSkirmishDefense;
    private final int baseCasualties;
    private final String name;
    private int currentSkirmishDefense;
    private int currentMorale;
    private int currentDiscipline;
    private int currentCasualties;
    private int baseChargeDefence;
    private DiceRoll skirmishAttack;
    private DiceRoll chargeAttack;
    private String faction;

    public Squad( String name, String faction, int baseSkirmishDefense, int baseDiscipline, int baseMorale, int baseCasualties, int baseChargeDefence, DiceRoll skirmish, DiceRoll charge) {
        this.baseSkirmishDefense = baseSkirmishDefense;
        this.baseDiscipline = baseDiscipline;
        this.baseMorale = baseMorale;
        this.baseCasualties = baseCasualties;
        this.baseChargeDefence = baseChargeDefence;
        this.name = name;
        this.currentMorale= this.baseMorale;
        this.currentDiscipline = this.baseDiscipline;
        this.currentSkirmishDefense = this.baseSkirmishDefense;
        this.currentCasualties = this.baseCasualties;
        this.skirmishAttack = skirmish;
        this.chargeAttack = charge;
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

    public int getSkirmishDefence(){
        return currentSkirmishDefense + currentDiscipline + currentMorale;
    }

    public int getChargeDefence(){
        return baseChargeDefence + currentMorale;
    }

    public int rollChargeAttack(int difficultyClass){
        int chargeSuccesses = chargeAttack.roll(difficultyClass, currentMorale);
        System.out.println(name + " charged against DC of "+ difficultyClass + " and succeeded "+chargeSuccesses+" attacks.");
        return chargeSuccesses;
    }

    public int rollSkirmishAttack(int difficultyClass) {
        int skirmishSucceses = skirmishAttack.roll(difficultyClass, currentDiscipline);
        System.out.println(name + " charged against DC of "+ difficultyClass + " and succeeded "+skirmishSucceses+" attacks.");

        return skirmishSucceses;
    }

    public String getCurrentStatus(){
        String nameLine = "Status Report for "+name+"\n";
        String casualtyLine = "Casualty: "+currentCasualties +"/"+baseCasualties+"\n";
        String moraleLine = "Morale: "+currentMorale +"/"+baseMorale+"\n";
        String disciplineLine = "Discipline: "+currentDiscipline +"/"+baseDiscipline+"\n";
        String defenseString = "Defences: \n";
        String skirmishDefenceLine = "Skirmish Defence: "+getSkirmishDefence()+"\n";
        String chargeDefenceLine = "Charge Defence: "+ getChargeDefence()+"\n";
        defenseString = defenseString + skirmishDefenceLine.indent(2)+ chargeDefenceLine.indent(2);
        String attackString = "Attacks: \n";
        String skirmishAttackLine = "Skirmish Attack: "+ skirmishAttack.toString()+" + Discipline"+"\n";
        String chargeAttackLine = "Charge Attack: "+ chargeAttack.toString()+" + Morale"+"\n";
        attackString = attackString + skirmishAttackLine.indent(2)+ chargeAttackLine.indent(2);
        return nameLine+casualtyLine+moraleLine+disciplineLine+defenseString+attackString;
    }

    public void exportAsCSV() throws IOException {
        File file = new File("OutputFiles/"+name+".csv");
        if(!file.createNewFile()){
            throw new IOException("File Already Exists");
        }
    }
}
