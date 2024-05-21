import Utils.DualPrintStream;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Citation: https://www.baeldung.com/java-write-console-output-file
 */
public class DualPrintStreamTest {
    final static List<String> OUTPUT_LINES = new ArrayList<String>() {{
        add("I came");
        add("I saw");
        add("I conquered");
    }};

    //TODO fix file path, add temp file to git ignore
    @Test
    public void whenUsingDualPrintStream_thenOutputsGoToConsoleAndFile() throws IOException {
        PrintStream originalOut = System.out;
        File outputFilePath = new File("src/test/java/TestFiles/dual-output.txt");
        DualPrintStream dualOut = new DualPrintStream(Files.newOutputStream(outputFilePath.toPath()), originalOut);
        System.setOut(dualOut);
        OUTPUT_LINES.forEach(line -> System.out.println(line));
        assertTrue(outputFilePath.exists(), "The file exists");
        assertLinesMatch(OUTPUT_LINES, Files.readAllLines(outputFilePath.toPath()));
        System.setOut(originalOut);
    }
}
