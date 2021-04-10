package interpreter;
import implementation.CustomImplementationFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ErrorCollector;
import util.PrintCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class InterpreterPrintStatementTest {

    private final PrintScriptInterpreter interpreter = new CustomImplementationFactory().interpreter();

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 0)
    public String version;

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 1)
    public String directory;

    @Parameterized.Parameters(name = "version {0} - {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"1.0", "arithmetic-operations"},
                {"1.0", "arithmetic-operations-decimal"},
                {"1.0", "simple-declare-assign"},
                {"1.0", "string-and-number-concat"},
                {"1.1", "if-statement"},
                {"1.1", "else-statement"}
        });
    }

    @Test
    public void testPrintStatement() throws FileNotFoundException {
        String testDirectory = "src/test/resources/print-statement/" + version + "/" + directory + "/";
        File srcFile = new File(testDirectory + "main.ps");
        List<String> expectedOutput = readLines(testDirectory + "output.txt");

        PrintCollector printCollector = new PrintCollector();
        ErrorCollector errorCollector = new ErrorCollector();
        interpreter.execute(srcFile, version, printCollector, errorCollector);

        assertThat(errorCollector.getErrors(), is(Collections.emptyList()));
        assertThat(printCollector.getMessages(), is(expectedOutput));
    }

    private List<String> readLines(String file) throws FileNotFoundException {
        Scanner s = new Scanner(new File(file));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();
        return list;
    }

}
