package Units;

import java.util.Arrays;

public enum Factions {
    ANARIC("Anaric"),
    BEASTMAN("Beastman"),
    GOBLIN("Goblin"),
    KAL_EDAR("Kal Edar"),
    MILITES_KYRIOS("Milites Kyrios"),
    ORCISH("Orcish"),
    VAL_DELAURE("Val DeLaure"),
    WOOD_ELVES("Wood Elves");

    private final String displayName;

    Factions(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Finds the faction with the given display name, ignoring case.
     */
    public static Factions fromDisplayName(String displayName) {
        for (Factions faction : values()) {
            if (faction.displayName.equalsIgnoreCase(displayName)) {
                return faction;
            }
        }
        throw new IllegalArgumentException("\"" + displayName + "\" is not a faction. The factions are: " + Arrays.toString(values()) + ".");
    }
}
