package Units;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Traits {
    private static final Traits INSTANCE = new Traits();
    private final HashMap<String, Trait> allTraits;

    private Traits() {
        allTraits = new HashMap<>();
        allTraits.put("Raise Spears (+2)",
                new Trait("Raise Spears (+2)", "Increase Charge Defense by 2 until beginning of next round."));
        allTraits.put("Raise Spears (+3)",
                new Trait("Raise Spears (+3)", "Increase Charge Defense by 3 until beginning of next round."));
        allTraits.put("Brace Pike (+4)",
                new Trait("Brace Pike (+4)", "Increase Charge Defense by 4 until beginning of next round."));
        allTraits.put("Shields Up (+2)",
                new Trait("Shields Up (+2)", "Increases Skirmish Defense by 2 until beginning of next round."));
        allTraits.put("Shield Wall",
                new Trait("Shield Wall", "This unit may use Shields Up Multiple times in a round. In addition, adjacent (but not diagonal) allied units gain it's shield bonus to Skirmish Defense"));
        allTraits.put("Combined Arms (Infantry +2)",
                new Trait("Combined Arms (Infantry +2)", "This unit gains +2 to all defense DCs when defending against an enemy that has been attacked by an allied Archer or Cavalry Unit."));
        allTraits.put("Loose Formation",
                new Trait("Loose Formation", "Allied units can make skirmish attacks through this unit, and overlap the back line of this unit's space. Overlapped Units count as one target for the purposes of Skirmish Attacks."));
    }

    public static Traits getInstance() {
        return INSTANCE;
    }

    public Map<String, Trait> getAllTraits() {
        return Collections.unmodifiableMap(allTraits);
    }

    public Trait getTrait(String traitName) {
        return allTraits.get(traitName);
    }
}
