package Units;

import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;

public class SkirmishWeapon {
    private String name;
    private HashMap<String, SkirmishAttack> attacks;
    private int modes;
    public SkirmishWeapon(String name, HashMap<String, SkirmishAttack> attacks){
        this.name = name;
        this.attacks = attacks;
        this.modes = attacks.size();
    }

    public HashMap<String, SkirmishAttack> getModes(){
        return attacks;
    }
    public String getName(){
        return name;
    }
    public boolean isExpended(){
        boolean isExpended = false;
        for (SkirmishAttack attack: attacks.values()){
            if (attack.isExpended()){
                isExpended = true;
            }
        }
        return  isExpended;
    }

    public String toString(){
        String output = "Name: "+name+"\nModes: "+ modes+"\n";
        for (SkirmishAttack attack : attacks.values()){
            output += attack.toString().indent(2);
        }
        return output;
    }
}
