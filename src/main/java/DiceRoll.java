import java.util.ArrayList;

public class DiceRoll {
    private Dice dice;
    private int numberOfDice;
    private int modifier;

    public DiceRoll (int dieSize, int numberOfDice, int modifier){
        this.dice = new Dice(dieSize);
        this.numberOfDice =numberOfDice;
        this.modifier=modifier;
    }

    public int roll(){
        int sum = 0;
        System.out.println("Rolling "+this.toString()+":");
        sum += dice.roll(numberOfDice);
        System.out.println("Sum of Dice = "+ sum);
        sum += modifier;
        System.out.println("Dice + Modifier = "+ sum);
        return sum;
    }

    @Override
    public String toString() {
        return numberOfDice+"d"+dice.getSize()+" + "+modifier;
    }
}

