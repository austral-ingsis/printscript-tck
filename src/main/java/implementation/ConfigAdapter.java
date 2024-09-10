package implementation;

import com.google.gson.annotations.SerializedName;
import com.printscript.linter.LinterConfig;

public class ConfigAdapter {
    public ConfigAdapter(
            String identifierFormat,
            boolean mandatoryVariableOrLiteralInPrintln,
            boolean mandatoryVariableOrLiteralInReadInput
    ) {
        this.identifierFormat = identifierFormat;
        this.mandatoryVariableOrLiteralInPrintln = mandatoryVariableOrLiteralInPrintln;
        this.mandatoryVariableOrLiteralInReadInput = mandatoryVariableOrLiteralInReadInput;
    }

    @SerializedName("identifier_format")
    public String identifierFormat;

    @SerializedName("mandatory-variable-or-literal-in-println")
    public Boolean mandatoryVariableOrLiteralInPrintln;

    @SerializedName("mandatory-variable-or-literal-in-readInput")
    public Boolean mandatoryVariableOrLiteralInReadInput;

    private String adaptCasing() {
        if (identifierFormat == null) {
            return null;
        }
        return switch (identifierFormat) {
            case "camel case" -> "camel";
            case "pascal case" -> "pascal";
            case "snake case" -> "snake";
            case "screaming snake case" -> "screaming_snake";
            case "kebab case" -> "kebab";
            case "screaming kebab case" -> "screaming_kebab";
            default -> identifierFormat;
        };
    }

    public LinterConfig adapt() {
        return new LinterConfig(
                adaptCasing(),
                mandatoryVariableOrLiteralInPrintln != null && mandatoryVariableOrLiteralInPrintln,
                mandatoryVariableOrLiteralInReadInput != null && mandatoryVariableOrLiteralInReadInput
        );
    }
}
