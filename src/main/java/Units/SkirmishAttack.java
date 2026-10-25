package Units;

public class SkirmishAttack {
    String name;
    int numberOfUses;
    boolean isInfinfite;
    DiceRoll diceRoll;
    int timesUsed = 0;
    private final int minRange;
    private final int maxRange;

    public SkirmishAttack(String name, DiceRoll diceRoll, boolean isInfinite, int numberOfUses, int minRange, int maxRange){
        if (isInfinite && numberOfUses!= -1){
            throw new IllegalArgumentException("For an infinite attack the number of uses should be -1");
        }else if (!isInfinite && !(numberOfUses > 0)){
            throw new IllegalArgumentException("For a finite attack, number of uses must be greater than 0");
        }
        validateRange(minRange, maxRange);
        this.name = name;
        this.diceRoll = diceRoll;
        this.isInfinfite = isInfinite;
        this.numberOfUses = numberOfUses;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    private static void validateRange(int minRange, int maxRange) {
        if (minRange < 0 || maxRange < minRange) {
            throw new IllegalArgumentException("Range must satisfy 0 <= minRange <= maxRange");
        }
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public boolean isInfinfite(){
        return isInfinfite;
    }

    public DiceRoll getRoll(){
        return diceRoll;
    }

    public boolean isExpended(){
        if (isInfinfite){
            return false;
        }
        return timesUsed >= numberOfUses;
    }
    public void useAttack(){
        if (!isInfinfite){
            if (timesUsed < numberOfUses){
                timesUsed++;
            }else{
                throw new IndexOutOfBoundsException("Attack has already been used maximum amount of times!");
            }
        }
    }
    public String toString(){
        String uses = "";
        if (!isInfinfite){
            uses = (numberOfUses-timesUsed)+ "/"+numberOfUses;
        }
        return name + ": " + diceRoll.toString()+" [" + minRange + "-" + maxRange + " ft] "+uses;
    }
}
