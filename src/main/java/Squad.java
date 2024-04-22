public class Squad {
    private final int baseMorale;
    private final int baseDiscipline;
    private final int baseArmor;
    private final int baseCasualties;
    private final String name;
    private int currentArmor;
    private int currentMorale;
    private int currentDiscipline;
    private int currentCasualties;
    private int baseChargeDefence;
    private DiceRoll skirmishAttack;
    private DiceRoll chargeAttack;
    private String faction;

    public Squad( String name, String faction, int baseArmor, int baseDiscipline, int baseMorale, int baseCasualties, int baseChargeDefence, DiceRoll skirmish, DiceRoll charge) {
        this.baseArmor = baseArmor;
        this.baseDiscipline = baseDiscipline;
        this.baseMorale = baseMorale;
        this.baseCasualties = baseCasualties;
        this.baseChargeDefence = baseChargeDefence;
        this.name = name;
        this.currentMorale= this.baseMorale;
        this.currentDiscipline = this.baseDiscipline;
        this.currentArmor = this.baseArmor;
        this.currentCasualties = this.baseCasualties;
        this.skirmishAttack = skirmish;
        this.chargeAttack = charge;
        this.faction= faction;
    }

    public int getBaseArmor() {
        return baseArmor;
    }

    public int getBaseMorale(){
        return baseMorale;
    }

    public int getBaseDiscipline() {
        return baseDiscipline;
    }

    public int getSkirmishDefence(){
        return currentArmor + currentDiscipline;
    }

    public int getChargeDefence(){
        return baseChargeDefence + currentMorale;
    }

    public int rollChargeAttack(){
        int chargeRoll = chargeAttack.roll();
        int total = chargeRoll + currentMorale;
        System.out.println("Morale Modifier = +"+currentMorale);
        System.out.println("Total = " + total);
        System.out.println(name + " charged for "+ chargeRoll +" + "+ currentMorale +"(Morale Bonus) for a total of "+total);
        return total;
    }

    public int rollSkirmishAttack() {
        int skirmishRoll = skirmishAttack.roll();
        int total = skirmishRoll + currentDiscipline;
        System.out.println("Discipline Modifier = +"+currentDiscipline);
        System.out.println("Total = " + total);
        System.out.println(name + " skirmished for "+ skirmishRoll +" + "+ currentDiscipline +"(Discipline Bonus) for a total of "+total);
        return total;
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
}
