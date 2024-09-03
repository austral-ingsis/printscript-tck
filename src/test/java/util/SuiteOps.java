package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuiteOps {

    public static List<Object[]> reversion(String version, List<Object[]> files) {
        return files.stream().map(obj -> {
           var copy = Arrays.stream(obj).toArray(Object[]::new);
           copy[0] = version;
           return copy;
        }).toList();
    }

    public static List<Object[]> getFilesForVersion(String basePath, String version, BiFunction<String, Path, Object[]> chooseFiles) throws IOException {
        try (Stream<Path> paths = Files.walk(getVersionSpecificPath(basePath, version))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map((Path p) -> chooseFiles.apply(version, p))
                    .collect(Collectors.toList());
        }
    }

    public static Path getVersionSpecificPath(String basePath, String version) {
        return Paths.get(basePath + version + "/");
    }

    public static File getInnerFile(Path test, String name) {
        return test.resolve(name).toFile();
    }

    public static Collection<Object[]> collectTestSet(String basePath) {
        return collectTestSet(basePath, (base, version) -> {
            try {
                return getFilesForVersion(base, version, collectFiles());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public static String readFile(File goldenFile) throws FileNotFoundException {
        final var reader = new BufferedReader(new FileReader(goldenFile));
        return reader.lines().collect(Collectors.joining("\n"));
    }


    public static Collection<Object[]> collectTestSet(String basePath, BiFunction<String, String, List<Object[]>> filePicker) {
        final List<Object[]> result = new java.util.ArrayList<>(List.of());
        final List<Object[]> version1 = filePicker.apply(basePath, "1.0");
        result.addAll(version1);
        result.addAll(reversion("1.1", version1));
        result.addAll(filePicker.apply(basePath, "1.1"));
        return result;
    }

    private static BiFunction<String, Path, Object[]> collectFiles() {
        return (version, path) -> new Object[]{version, path.toFile()};
    }
}
