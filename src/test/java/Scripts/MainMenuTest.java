package Scripts;

import Units.Squad;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainMenuTest {
    @Test
    public void readYesNoAsksAgainUntilTheAnswerIsYOrN() {
        assertTrue(MainMenu.readYesNo(new Scanner("yes\n\ny\n"), "Continue? (Y/N)"));
        assertFalse(MainMenu.readYesNo(new Scanner("maybe\n N \n"), "Continue? (Y/N)"));
    }

    @Test
    public void importSquadLoadsTheFileAtTheEnteredPath() {
        Squad squad = MainMenu.importSquad(new Scanner(" squads/garrison-1.json \n"));

        assertNotNull(squad);
        assertTrue(squad.getCurrentStatus().startsWith("Status Report for Garrison 1"), squad.getCurrentStatus());
    }

    @Test
    public void importSquadSaysWhatIsWrongWithTheFile(@TempDir Path folder) throws IOException {
        Path file = Files.writeString(folder.resolve("squad.json"), "{ \"moral\": 10 }");

        String printed = printedBy(() -> assertNull(MainMenu.importSquad(new Scanner(file + "\n"))));

        assertTrue(printed.contains("\"moral\""), printed);
    }

    @Test
    public void importSquadSaysWhenThereIsNoFile(@TempDir Path folder) {
        Path missing = folder.resolve("missing.json");

        String printed = printedBy(() -> assertNull(MainMenu.importSquad(new Scanner(missing + "\n"))));

        assertTrue(printed.contains("No file found at " + missing), printed);
    }

    private static String printedBy(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream printed = new ByteArrayOutputStream();
        System.setOut(new PrintStream(printed));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return printed.toString();
    }
}
