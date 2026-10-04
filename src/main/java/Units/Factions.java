package Units;

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
}
