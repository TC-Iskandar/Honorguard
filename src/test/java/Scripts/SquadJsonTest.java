package Scripts;

import Units.Squad;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadJsonTest {
    // Every stat is different, so a value read into the wrong place shows up in the status report.
    private static final String RANGERS = """
            {
              "name": "Rangers",
              "faction": "Wood Elves",
              "morale": 10,
              "discipline": 2,
              "casualties": 6,
              "skirmishDefense": 4,
              "meleeDefense": 5,
              "chargeDefense": 3,
              "chargeAttack": 1,
              "skirmishWeapons": [
                {
                  "name": "Bow",
                  "modes": [
                    { "name": "Volley", "numberOfDice": 2, "dieSize": 6, "modifier": 3, "uses": 3 },
                    { "name": "Aimed", "numberOfDice": 1, "dieSize": 8, "modifier": 4 }
                  ]
                },
                {
                  "name": "Knives",
                  "modes": [
                    { "name": "Throw", "numberOfDice": 2, "dieSize": 4, "modifier": 0, "uses": 2 }
                  ]
                }
              ],
              "meleeWeapons": [
                {
                  "name": "Sword",
                  "modes": [
                    { "name": "Slash", "numberOfDice": 1, "dieSize": 6, "modifier": 2 }
                  ]
                }
              ]
            }
            """;

    private static List<String> statusLines(Squad squad) {
        return squad.getCurrentStatus().lines().map(String::strip).toList();
    }

    /** RANGERS with one piece of text replaced. Fails if the text is not there, so a test cannot pass by accident. */
    private static String rangersWith(String text, String replacement) {
        assertTrue(RANGERS.contains(text), "RANGERS does not contain " + text);
        return RANGERS.replace(text, replacement);
    }

    private static String importError(String json) {
        return assertThrows(IllegalArgumentException.class, () -> SquadJson.fromJson(json)).getMessage();
    }

    @Test
    public void importsTheSquadsStatsAndWeapons() {
        Squad squad = SquadJson.fromJson(RANGERS);

        List<String> lines = statusLines(squad);
        for (String expected : List.of(
                "Status Report for Rangers",
                "Faction: Wood Elves",
                "Casualty: 6/6",
                "Morale: 10/10",
                "Discipline: 2/2",
                "Skirmish Defence: 6",          // 4 + discipline 2
                "Melee Defence: 17",            // 5 + morale 10 + discipline 2
                "Charge Defence: 25",           // 10 + 3 + morale 10 + discipline 2
                "Name: Bow",
                "Volley: 2d6 + 3 3/3",          // has uses, so it is limited
                "Aimed: 1d8 + 4",               // no uses, so it is unlimited
                "Name: Knives",
                "Throw: 2d4 + 0 2/2",
                "Name: Sword",
                "Slash: 1d6 + 2",
                "Charge Attack: 6 times 1d20 + 1 + 12")) {
            assertTrue(lines.contains(expected), "missing \"" + expected + "\" in:\n" + squad.getCurrentStatus());
        }
    }

    @Test
    public void importsTheGarrisonExampleFile() throws IOException {
        Squad squad = SquadJson.load(Path.of("squads", "garrison-spearman.json"));

        List<String> lines = statusLines(squad);
        assertTrue(lines.contains("Status Report for Lorrainean Garrison Spearman"), squad.getCurrentStatus());
        assertTrue(lines.contains("Throw!!: 6d6 + 3 1/1"), squad.getCurrentStatus());
    }

    @Test
    public void rejectsAMisspelledFieldAndSaysWhichLine() {
        String message = importError(rangersWith("\"morale\"", "\"moral\""));

        assertTrue(message.contains("\"moral\""), message);
        assertTrue(message.contains("Line 4"), message);
    }

    @Test
    public void rejectsBrokenJsonAndSaysWhichLine() {
        String message = importError(rangersWith("\"discipline\": 2,", "\"discipline\": 2"));

        assertTrue(message.contains("Line 6"), message);
    }

    @Test
    public void rejectsAMissingField() {
        String message = importError(rangersWith("\"casualties\": 6,\n", ""));

        assertTrue(message.contains("\"casualties\""), message);
    }

    @Test
    public void rejectsAModeWithAMissingFieldAndSaysWhichMode() {
        String message = importError(rangersWith("\"name\": \"Aimed\", \"numberOfDice\": 1, ", "\"name\": \"Aimed\", "));

        assertTrue(message.contains("\"numberOfDice\""), message);
        assertTrue(message.contains("Bow"), message);
        assertTrue(message.contains("Aimed"), message);
    }

    @Test
    public void rejectsAnInvalidDieSizeAndSaysWhichMode() {
        String message = importError(rangersWith("\"dieSize\": 8", "\"dieSize\": 7"));

        assertTrue(message.contains("d7"), message);
        assertTrue(message.contains("Bow"), message);
        assertTrue(message.contains("Aimed"), message);
    }

    @Test
    public void rejectsZeroUsesAndSaysWhichMode() {
        String message = importError(rangersWith("\"modifier\": 3, \"uses\": 3", "\"modifier\": 3, \"uses\": 0"));

        assertTrue(message.contains("Bow"), message);
        assertTrue(message.contains("Volley"), message);
    }

    @Test
    public void rejectsTwoModesWithTheSameName() {
        String message = importError(rangersWith("\"Aimed\"", "\"Volley\""));

        assertTrue(message.contains("Bow"), message);
        assertTrue(message.contains("Volley"), message);
    }

    @Test
    public void rejectsTwoWeaponsWithTheSameName() {
        String message = importError(rangersWith("\"Knives\"", "\"Bow\""));

        assertTrue(message.contains("Bow"), message);
    }

    @Test
    public void rejectsTwoMeleeModesWithTheSameName() {
        String slash = "{ \"name\": \"Slash\", \"numberOfDice\": 1, \"dieSize\": 6, \"modifier\": 2 }";
        String message = importError(rangersWith(slash, slash + ", " + slash));

        assertTrue(message.contains("Sword"), message);
        assertTrue(message.contains("Slash"), message);
    }

    @Test
    public void rejectsTwoMeleeWeaponsWithTheSameName() {
        String message = importError(rangersWith("\"meleeWeapons\": [",
                "\"meleeWeapons\": [ { \"name\": \"Sword\", \"modes\": [ { \"name\": \"Stab\", \"numberOfDice\": 1, \"dieSize\": 4, \"modifier\": 0 } ] },"));

        assertTrue(message.contains("Sword"), message);
    }

    @Test
    public void rejectsAWeaponWithoutAName() {
        String message = importError(rangersWith("\"name\": \"Knives\",", ""));

        assertTrue(message.contains("\"name\""), message);
    }

    @Test
    public void rejectsAnUnknownFactionAndListsTheFactions() {
        String message = importError(rangersWith("Wood Elves", "Atlantis"));

        assertTrue(message.contains("Atlantis"), message);
        assertTrue(message.contains("Val DeLaure"), message);
    }

    @Test
    public void acceptsAFactionNameInAnyCase() {
        Squad squad = SquadJson.fromJson(rangersWith("Wood Elves", "wood elves"));

        assertTrue(statusLines(squad).contains("Faction: Wood Elves"), squad.getCurrentStatus());
    }

    @Test
    public void rejectsUsesOnAMeleeMode() {
        String message = importError(rangersWith("\"modifier\": 2 }", "\"modifier\": 2, \"uses\": 2 }"));

        assertTrue(message.contains("Sword"), message);
        assertTrue(message.contains("Slash"), message);
        assertTrue(message.contains("\"uses\""), message);
    }
}
