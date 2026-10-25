package Units;

public class MeleeAttack {
    private String name;
    private DiceRoll diceRoll;
    private final int minRange;
    private final int maxRange;

    public MeleeAttack(String name, DiceRoll diceRoll, int minRange, int maxRange) {
        if (minRange < 0 || maxRange < minRange) {
            throw new IllegalArgumentException("Range must satisfy 0 <= minRange <= maxRange");
        }
        this.name = name;
        this.diceRoll = diceRoll;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public DiceRoll getRoll() {
        return diceRoll;
    }

    public String toString() {
        return name + ": " + diceRoll.toString() + " [" + minRange + "-" + maxRange + " ft]";
    }
}
