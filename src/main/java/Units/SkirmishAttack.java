package Units;

public class SkirmishAttack {
    String name;
    int numberOfUses;
    boolean isInfinfite;
    DiceRoll diceRoll;
    int timesUsed = 0;
    public SkirmishAttack(String name, DiceRoll diceRoll, boolean isInfinfite, int numberOfUses){
        if (isInfinfite && numberOfUses!= -1){
            throw new IllegalArgumentException("For an infinite attack the number of uses should be -1");
        }else if (!isInfinfite && !(numberOfUses > 0)){
            throw new IllegalArgumentException("For a finite attack, number of uses must be greater than 0");
        }
        this.name = name;
        this.diceRoll = diceRoll;
        this.isInfinfite = isInfinfite;
        this.numberOfUses = numberOfUses;
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
        return name + ": " + diceRoll.toString()+" "+uses;
    }
}
