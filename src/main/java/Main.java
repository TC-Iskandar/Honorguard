//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //TODO Create Test Suite
    //TODO Print to Text File
    //TODO Create State Manager File
    //TODO Implementing Fighting
    //TODO Implement Unit Export
    //TODO Implement Unit Import

    public static void main(String[] args) {
        Squad garrison = makeGarrison();
        System.out.println(garrison.getCurrentStatus());

        System.out.println("\nRolling Garrison Skirmish Attack!");
        garrison.rollSkirmishAttack();
        System.out.println("\nRolling Garrison Charge Attack!");
        garrison.rollChargeAttack();

        Squad goblins = makeGoblins();
        System.out.println("\n \n"+goblins.getCurrentStatus());

        System.out.println("\nRolling Goblin Skirmish Attack!");
        goblins.rollSkirmishAttack();
        System.out.println("\nRolling Goblin Charge Attack!");
        goblins.rollChargeAttack();

    }

    private static Squad makeGoblins(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return  new Squad(1, 0, 5, 6, "Goblin Squad", 4, skirmish, charge);
    }

    private static Squad makeGarrison(){
        DiceRoll skirmish = new DiceRoll(6, 4, 0);
        DiceRoll charge = new DiceRoll(20, 1, 7);
        return  new Squad(3, 2, 10, 8, "Garrison Spearmen", 8, skirmish, charge);
    }
}