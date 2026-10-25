package Scripts;

import Units.Squad;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class InitializationScriptTest {
    @Test
    public void userMadeSquadAsksAgainUntilEachAnswerIsValid() {
        Scanner in = new Scanner(String.join("\n",
                "Rangers",              // name
                "0", "9", "8",          // faction: only 1 to 8 are listed, 8 is Wood Elves
                "abc", "10",            // morale: not a number
                " 2 ",                  // discipline: surrounding spaces are ignored
                "", "6",                // casualties: empty line
                "4", "5", "3",          // skirmish defence, melee defence, charge defence
                "-1", "2",              // traits: must be at least 0
                "Not A Trait", "Shield Wall",
                "Shield Wall", "Loose Formation",
                "1", "0", "20",        // charge attack, min/max charge range
                "0", "1",               // skirmish weapons: must be at least 1
                "Bow",
                "0", "2",               // modes: must be at least 1
                "Volley",
                "5", "6",               // die size: 5 is not a valid die
                "0", "2",               // number of dice: must be at least 1
                "3", "0", "25", "false", // modifier, min/max range, infinite
                "0", "3",               // uses: must be at least 1
                "Volley", "Aimed",      // mode name: Volley is already taken
                "8", "1", "4", "0", "30",
                "yes", "TRUE",          // infinite: only true or false
                "-1", "1",              // melee weapons: must be at least 1
                "Sword",
                "0", "1",               // modes: must be at least 1
                "Slash",
                "7", "6",               // die size: 7 is not a valid die
                "-1", "1",              // number of dice: must be at least 1
                "2", "0", "5"          // modifier, min/max range
        ) + "\n");

        Squad squad = assertDoesNotThrow(() -> InitializationScript.userMadeSquad(in));

        assertEquals(10, squad.getBaseMorale());
        assertEquals(2, squad.getBaseDiscipline());
        assertEquals(0, squad.getChargeMinRange());
        assertEquals(20, squad.getChargeMaxRange());
        String status = squad.getCurrentStatus();
        List<String> lines = status.lines().map(String::strip).toList();
        assertTrue(lines.contains("Faction: Wood Elves"), status);
        assertTrue(lines.contains("Casualty: 6/6"), status);
        assertTrue(lines.contains("Name: Shield Wall"), status);
        assertTrue(lines.contains("Name: Loose Formation"), status);
        assertTrue(lines.contains("Volley: 2d6 + 3 [0-25 ft] 3/3"), status);
        assertTrue(lines.contains("Aimed: 1d8 + 4 [0-30 ft]"), status);
        assertTrue(lines.contains("Slash: 1d6 + 2 [0-5 ft]"), status);
        assertTrue(lines.contains("Charge Attack: 6 times 1d20 + 1 [0-20 ft] + 12"), status);
        assertFalse(in.hasNextLine(), "every scripted answer should have been used");
    }

    @Test
    public void builtInSquadsDisplayConfiguredAttackRanges() {
        String goblinStatus = InitializationScript.makeGoblins().getCurrentStatus();
        assertTrue(goblinStatus.contains("Dogslicers: 4d4 + 2 [0-25 ft]"), goblinStatus);
        assertTrue(goblinStatus.contains("Slings: 4d4 + 2 [0-50 ft] 15/15"), goblinStatus);
        assertTrue(goblinStatus.contains("Dogslicers: 5d6 + 5 [0-5 ft]"), goblinStatus);
        assertTrue(goblinStatus.contains("Charge Attack: 14 times 1d20 + 1 [0-30 ft] + 8"), goblinStatus);

        String garrisonStatus = InitializationScript.makeGarrison().getCurrentStatus();
        assertTrue(garrisonStatus.contains("Spears: 4d6 + 2 [0-25 ft]"), garrisonStatus);
        assertTrue(garrisonStatus.contains("Throw!!: 6d6 + 3 [0-30 ft] 1/1"), garrisonStatus);
        assertTrue(garrisonStatus.contains("Sidearms: 4d4 + 2 [0-25 ft]"), garrisonStatus);
        assertTrue(garrisonStatus.contains("Spears: 4d8 + 2 [0-10 ft]"), garrisonStatus);
        assertTrue(garrisonStatus.contains("Sidearms: 4d4 + 2 [0-5 ft]"), garrisonStatus);
        assertTrue(garrisonStatus.contains("Charge Attack: 8 times 1d20 + 3 [0-25 ft] + 15"), garrisonStatus);
    }
}
