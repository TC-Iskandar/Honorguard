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
        int successes = 0;
        System.out.println("Rolling "+this.toString()+":");

        for (int i=0; i<numberOfDice; i++){
            int roll = dice.roll(1);
            System.out.println("Dice = "+ roll);
            int sum = roll + totalModifier;
            System.out.println("Dice + Modifier = "+ sum);
            if (sum> difficultyClass){
                successes++;
                System.out.println("Attack Succesful!");
            }else {
                System.out.println("Attack Failed!");
            }
        }
        System.out.println("Number of Successes = "+ successes);
        return successes;
    }

    @Override
    public String toString() {
        return numberOfDice+"d"+dice.getSize()+" + "+ baseModifier;
    }
}

