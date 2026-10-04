package Units;

import java.util.HashMap;

public class MeleeWeapon {
    private String name;
    private HashMap<String, MeleeAttack> attacks;
    private int modes;

    public MeleeWeapon(String name, HashMap<String, MeleeAttack> attacks) {
        this.name = name;
        this.attacks = attacks;
        this.modes = attacks.size();
    }

    public HashMap<String, MeleeAttack> getModes() {
        return attacks;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        String output = "Name: " + name + "\nModes: " + modes + "\n";
        for (MeleeAttack attack : attacks.values()) {
            output += attack.toString().indent(2);
        }
        return output;
    }
}
