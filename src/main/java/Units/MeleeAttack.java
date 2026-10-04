package Units;

public class MeleeAttack {
    private String name;
    private DiceRoll diceRoll;

    public MeleeAttack(String name, DiceRoll diceRoll) {
        this.name = name;
        this.diceRoll = diceRoll;
    }

    public DiceRoll getRoll() {
        return diceRoll;
    }

    public String toString() {
        return name + ": " + diceRoll.toString();
    }
}
