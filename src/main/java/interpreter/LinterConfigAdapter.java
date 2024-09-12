package interpreter;

import com.fasterxml.jackson.annotation.JsonProperty;
import linter.LinterConfig;

public class LinterConfigAdapter {
    private boolean allow_expression_in_println;
    private boolean allow_expression_in_readinput;
    private String naming_convention;

    @JsonProperty("mandatory-variable-or-literal-in-println")
    public void setAllow_expression_in_println(boolean allow_expression_in_println) {
        this.allow_expression_in_println = !allow_expression_in_println;
    }

    @JsonProperty("mandatory-variable-or-literal-in-readInput")
    public void setAllow_expression_in_readinput(boolean allow_expression_in_readinput) {
        this.allow_expression_in_readinput = !allow_expression_in_readinput;
    }

    @JsonProperty("identifier_format")
    public void setNaming_convention(String naming_convention) {
        this.naming_convention = naming_convention.replace(" ", "_" );
    }

    public LinterConfig getConfig(){
        return new LinterConfig(allow_expression_in_println, allow_expression_in_readinput, naming_convention);
    }
}
