package Scripts;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainMenuTest {
    @Test
    public void readYesNoAsksAgainUntilTheAnswerIsYOrN() {
        assertTrue(MainMenu.readYesNo(new Scanner("yes\n\ny\n"), "Continue? (Y/N)"));
        assertFalse(MainMenu.readYesNo(new Scanner("maybe\n N \n"), "Continue? (Y/N)"));
    }
}
