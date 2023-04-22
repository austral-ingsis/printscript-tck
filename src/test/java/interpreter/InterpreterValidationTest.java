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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

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
    public static Collection<Object[]> data() throws IOException {
        final List<Object[]> result = getFilesForVersion("1.0");
        result.addAll(getFilesForVersion("1.1"));
        return result;
    }

    private static List<Object[]> getFilesForVersion(String version) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(basePath + version + "/"))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String fileName = p.getFileName().toString();
                        return fileName.startsWith("valid") || fileName.startsWith("invalid");
                    })
                    .map((Path p) -> new Object[]{version, p.toFile()})
                    .collect(Collectors.toList());
        }
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
