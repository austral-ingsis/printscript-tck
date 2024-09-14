package implementation;

import com.google.gson.annotations.SerializedName;
import com.printscript.formatter.FormatterConfig;

public class FormatterConfigAdapter {
    public FormatterConfigAdapter(
            Integer lineBreaksAfterPrints,
            Boolean spaceAroundEquals,
            Boolean noSpaceAroundEquals,
            Boolean spaceBeforeColon,
            Boolean spaceAfterColon,
            Boolean ifBraceSameLine,
            Boolean ifBraceBelowLine,
            Integer indent
    ) {
        this.lineBreaksAfterPrints = lineBreaksAfterPrints;
        this.spaceAroundEquals = spaceAroundEquals;
        this.noSpaceAroundEquals = noSpaceAroundEquals;
        this.spaceBeforeColon = spaceBeforeColon;
        this.spaceAfterColon = spaceAfterColon;
        this.ifBraceSameLine = ifBraceSameLine;
        this.ifBraceBelowLine = ifBraceBelowLine;
        this.indent = indent;
    }

    @SerializedName("line-breaks-after-println")
    Integer lineBreaksAfterPrints;
    @SerializedName("enforce-spacing-around-equals")
    Boolean spaceAroundEquals;
    @SerializedName("enforce-no-spacing-around-equals")
    Boolean noSpaceAroundEquals;
    @SerializedName("enforce-spacing-before-colon-in-declaration")
    Boolean spaceBeforeColon;
    @SerializedName("enforce-spacing-after-colon-in-declaration")
    Boolean spaceAfterColon;
    @SerializedName("if-brace-same-line")
    Boolean ifBraceSameLine;
    @SerializedName("if-brace-below-line")
    Boolean ifBraceBelowLine;
    @SerializedName("indent-inside-if")
    Integer indent;

    public FormatterConfig adapt() {
        return new FormatterConfig(
                lineBreaksAfterPrints,
                spaceAroundEquals != null && spaceAroundEquals,
                noSpaceAroundEquals != null && noSpaceAroundEquals,
                spaceBeforeColon != null && spaceBeforeColon,
                spaceAfterColon != null && spaceAfterColon,
                ifBraceSameLine != null && ifBraceSameLine,
                ifBraceBelowLine != null && ifBraceBelowLine,
                indent
        );
    }
}
