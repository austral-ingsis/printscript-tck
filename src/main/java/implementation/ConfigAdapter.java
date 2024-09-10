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

    public LinterConfig adapt() {
        return new LinterConfig(
                identifierFormat,
                mandatoryVariableOrLiteralInPrintln != null && mandatoryVariableOrLiteralInPrintln,
                mandatoryVariableOrLiteralInReadInput != null && mandatoryVariableOrLiteralInReadInput
        );
    }
}
