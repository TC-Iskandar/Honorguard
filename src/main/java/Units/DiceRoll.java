package Units;

public class DiceRoll {
    private Dice dice;
    private int numberOfDice;
    private int baseModifier;

    public DiceRoll (int dieSize, int numberOfDice, int baseModifier){
        this.dice = new Dice(dieSize);
        this.numberOfDice =numberOfDice;
        this.baseModifier = baseModifier;
    }

    public int roll(int difficultyClass, int additionalModifier){
        int totalModifier= baseModifier + additionalModifier;
        int overflow = 0;
        System.out.println("Rolling "+this.toString()+":");


            int roll = dice.roll(numberOfDice);
            System.out.println("Units.Dice = "+ roll);
            int sum = roll + totalModifier;
            System.out.println("Units.Dice + Modifier = "+ sum);
            if (sum> difficultyClass){
                overflow = sum - difficultyClass;
                System.out.println("Attack Succesful!");
            }else {
                System.out.println("Attack Failed!");
            }
        return overflow;
    }

    @Override
    public String toString() {
        return numberOfDice+"d"+dice.getSize()+" + "+ baseModifier;
    }
}

