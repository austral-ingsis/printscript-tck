package implementation;

final class LanguageVersion {

    private static final String VERSION_ONE = "1.0";
    private static final String VERSION_ONE_ONE = "1.1";

    private LanguageVersion() {
    }

    static boolean isVersionOne(String version) {
        return VERSION_ONE.equals(version);
    }

    static boolean isVersionOneOne(String version) {
        return VERSION_ONE_ONE.equals(version);
    }

    static boolean isSupported(String version) {
        return isVersionOne(version) || isVersionOneOne(version);
    }

    static void requireSupported(String version) {
        if (!isSupported(version)) {
            throw new UnsupportedOperationException(unsupportedVersionMessage(version));
        }
    }

    static String unsupportedVersionMessage(String version) {
        return "PrintScript version " + version + " is not implemented yet";
    }
}
