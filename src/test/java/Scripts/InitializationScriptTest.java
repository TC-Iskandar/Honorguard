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
                "4", "5", "3", "1",     // skirmish defence, melee defence, charge defence, charge attack
                "0", "1",               // skirmish weapons: must be at least 1
                "Bow",
                "0", "2",               // modes: must be at least 1
                "Volley",
                "5", "6",               // die size: 5 is not a valid die
                "0", "2",               // number of dice: must be at least 1
                "3", "false",           // modifier, infinite
                "0", "3",               // uses: must be at least 1
                "Volley", "Aimed",      // mode name: Volley is already taken
                "8", "1", "4",
                "yes", "TRUE",          // infinite: only true or false
                "-1", "1",              // melee weapons: must be at least 1
                "Sword",
                "0", "1",               // modes: must be at least 1
                "Slash",
                "7", "6",               // die size: 7 is not a valid die
                "-1", "1",              // number of dice: must be at least 1
                "2"                     // modifier
        ) + "\n");

        Squad squad = assertDoesNotThrow(() -> InitializationScript.userMadeSquad(in));

        assertEquals(10, squad.getBaseMorale());
        assertEquals(2, squad.getBaseDiscipline());
        String status = squad.getCurrentStatus();
        List<String> lines = status.lines().map(String::strip).toList();
        assertTrue(lines.contains("Faction: Wood Elves"), status);
        assertTrue(lines.contains("Casualty: 6/6"), status);
        assertTrue(lines.contains("Volley: 2d6 + 3 3/3"), status);
        assertTrue(lines.contains("Aimed: 1d8 + 4"), status);
        assertTrue(lines.contains("Slash: 1d6 + 2"), status);
        assertFalse(in.hasNextLine(), "every scripted answer should have been used");
    }
}
