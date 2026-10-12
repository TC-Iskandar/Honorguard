package Scripts;

import Units.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Imports a Squad from JSON. The JSON is first read into the classes below, which have the same shape as the file.
 * The Squad is then built with the same constructors InitializationScript uses, so an imported squad gets the same
 * checks as one typed in by hand.
 */
public class SquadJson {
    private static final ObjectMapper mapper = new ObjectMapper();

    // These are classes with public fields rather than records because Jackson fills fields in one at a time, so it
    // reports a misspelled field on its own line. With a record it only notices at the end of the object.
    private static class SquadData {
        public String name;
        public String faction;
        public Integer morale;
        public Integer discipline;
        public Integer casualties;
        public Integer skirmishDefense;
        public Integer meleeDefense;
        public Integer chargeDefense;
        public Integer chargeAttack;
        public List<WeaponData> skirmishWeapons;
        public List<WeaponData> meleeWeapons;
    }

    private static class WeaponData {
        public String name;
        public List<ModeData> modes;
    }

    /** A mode without uses is unlimited. Only skirmish modes can have uses. */
    private static class ModeData {
        public String name;
        public Integer numberOfDice;
        public Integer dieSize;
        public Integer modifier;
        public Integer uses;
    }

    /**
     * Reads the squad in a JSON file.
     * @param file Path of the JSON file
     * @return The imported Squad.
     * @throws IOException if the file cannot be read.
     * @throws IllegalArgumentException if the file is not a valid squad. The message says what is wrong.
     */
    public static Squad load(Path file) throws IOException {
        return fromJson(Files.readString(file));
    }

    static Squad fromJson(String json) {
        SquadData squad = parse(json);
        String where = "The squad";
        String name = require(squad.name, "name", where);
        Factions faction = Factions.fromDisplayName(require(squad.faction, "faction", where));
        int morale = require(squad.morale, "morale", where);
        int discipline = require(squad.discipline, "discipline", where);
        int casualties = require(squad.casualties, "casualties", where);
        int skirmishDefense = require(squad.skirmishDefense, "skirmishDefense", where);
        int meleeDefense = require(squad.meleeDefense, "meleeDefense", where);
        int chargeDefense = require(squad.chargeDefense, "chargeDefense", where);
        DiceRoll charge = new DiceRoll(20, 1, require(squad.chargeAttack, "chargeAttack", where));
        HashMap<String, SkirmishWeapon> skirmishWeapons = toSkirmishWeapons(require(squad.skirmishWeapons, "skirmishWeapons", where));
        HashMap<String, MeleeWeapon> meleeWeapons = toMeleeWeapons(require(squad.meleeWeapons, "meleeWeapons", where));
        return new Squad(name, faction, morale, discipline, casualties, skirmishDefense, meleeDefense, chargeDefense,
                skirmishWeapons, charge, meleeWeapons);
    }

    /**
     * Reads the JSON into the classes above. Mistakes in the JSON itself, like a misspelled field, are reported with
     * their line.
     */
    private static SquadData parse(String json) {
        try {
            return mapper.readValue(json, SquadData.class);
        } catch (JsonProcessingException e) {
            String problem = e instanceof UnrecognizedPropertyException unknown
                    ? "unknown field \"" + unknown.getPropertyName() + "\"."
                    : e.getOriginalMessage();
            throw new IllegalArgumentException("Line " + e.getLocation().getLineNr() + ": " + problem, e);
        }
    }

    private static HashMap<String, SkirmishWeapon> toSkirmishWeapons(List<WeaponData> weapons) {
        HashMap<String, SkirmishWeapon> skirmishWeapons = new HashMap<>();
        for (WeaponData weapon : weapons) {
            String weaponName = require(weapon.name, "name", "A skirmish weapon");
            if (skirmishWeapons.containsKey(weaponName)) {
                throw new IllegalArgumentException("There are two skirmish weapons named \"" + weaponName + "\".");
            }
            String weaponWhere = "Skirmish weapon \"" + weaponName + "\"";
            HashMap<String, SkirmishAttack> attacks = new HashMap<>();
            for (ModeData mode : require(weapon.modes, "modes", weaponWhere)) {
                String modeName = require(mode.name, "name", "A mode of skirmish weapon \"" + weaponName + "\"");
                if (attacks.containsKey(modeName)) {
                    throw new IllegalArgumentException(weaponWhere + " has two modes named \"" + modeName + "\".");
                }
                String where = weaponWhere + ", mode \"" + modeName + "\"";
                DiceRoll roll = toDiceRoll(mode, where);
                boolean infinite = mode.uses == null;
                int uses = infinite ? -1 : mode.uses;
                attacks.put(modeName, withContext(where, () -> new SkirmishAttack(modeName, roll, infinite, uses)));
            }
            skirmishWeapons.put(weaponName, new SkirmishWeapon(weaponName, attacks));
        }
        return skirmishWeapons;
    }

    private static HashMap<String, MeleeWeapon> toMeleeWeapons(List<WeaponData> weapons) {
        HashMap<String, MeleeWeapon> meleeWeapons = new HashMap<>();
        for (WeaponData weapon : weapons) {
            String weaponName = require(weapon.name, "name", "A melee weapon");
            if (meleeWeapons.containsKey(weaponName)) {
                throw new IllegalArgumentException("There are two melee weapons named \"" + weaponName + "\".");
            }
            String weaponWhere = "Melee weapon \"" + weaponName + "\"";
            HashMap<String, MeleeAttack> attacks = new HashMap<>();
            for (ModeData mode : require(weapon.modes, "modes", weaponWhere)) {
                String modeName = require(mode.name, "name", "A mode of melee weapon \"" + weaponName + "\"");
                if (attacks.containsKey(modeName)) {
                    throw new IllegalArgumentException(weaponWhere + " has two modes named \"" + modeName + "\".");
                }
                String where = weaponWhere + ", mode \"" + modeName + "\"";
                if (mode.uses != null) {
                    throw new IllegalArgumentException(where + " has \"uses\", but only skirmish modes can have uses.");
                }
                attacks.put(modeName, new MeleeAttack(modeName, toDiceRoll(mode, where)));
            }
            meleeWeapons.put(weaponName, new MeleeWeapon(weaponName, attacks));
        }
        return meleeWeapons;
    }

    private static DiceRoll toDiceRoll(ModeData mode, String where) {
        int dieSize = require(mode.dieSize, "dieSize", where);
        int numberOfDice = require(mode.numberOfDice, "numberOfDice", where);
        int modifier = require(mode.modifier, "modifier", where);
        return withContext(where, () -> new DiceRoll(dieSize, numberOfDice, modifier));
    }

    /**
     * Returns the value, or says which field is missing if the file left it out.
     */
    private static <T> T require(T value, String field, String where) {
        if (value == null) {
            throw new IllegalArgumentException(where + " is missing \"" + field + "\".");
        }
        return value;
    }

    /**
     * Runs a constructor and puts where in front of its error message, so the user can find the problem in the file.
     */
    private static <T> T withContext(String where, Supplier<T> constructor) {
        try {
            return constructor.get();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(where + ": " + e.getMessage(), e);
        }
    }
}
