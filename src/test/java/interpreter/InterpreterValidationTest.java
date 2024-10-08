package interpreter;

import implementation.CustomImplementationFactory;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ErrorCollector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static util.SuiteOps.*;

@RunWith(Parameterized.class)
public class InterpreterValidationTest {

    private static final String basePath = "src/test/resources/validation/";
    private final PrintScriptInterpreter interpreter = new CustomImplementationFactory().interpreter();

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 0)
    public String version;

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 1)
    public File file;

    @Parameterized.Parameters(name = "version {0} - {1})")
    public static Collection<Object[]> data() {
        // We don't want tests from previous versions as they are already duplicate. We will have to change this if there ever is a version 1.2
        return collectTestSet(basePath, false);
    }

    @Test
    public void testValidation() throws FileNotFoundException {
        ErrorCollector errorCollector = new ErrorCollector();
        final var fileInputStream = new FileInputStream(file);
        interpreter.execute(fileInputStream, version, (msg) -> {}, errorCollector, (name) -> name);
        boolean shouldBeValid = file.getName().startsWith("valid");
        final Matcher<List<String>> errorMatcher = getErrorMatcherForExpectedResult(shouldBeValid);
        assertThat(errorCollector.getErrors(), errorMatcher);
    }

    private Matcher<List<String>> getErrorMatcherForExpectedResult(boolean shouldBeValid) {
        return shouldBeValid ? is(Collections.emptyList()) : not(Collections.emptyList());
    }

}
