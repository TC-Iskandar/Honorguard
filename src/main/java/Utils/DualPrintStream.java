package Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Citation: https://www.baeldung.com/java-write-console-output-file
 */
public class DualPrintStream extends PrintStream {
    private final PrintStream second;

    public DualPrintStream(OutputStream main, PrintStream second) {
        super(main);
        this.second = second;
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        second.write(b);
    }

    @Override
    public void println(String string){
        super.println(string);
        second.println(string);
    }

    @Override
    public void close(){
        super.close();
        second.close();
    }
}

