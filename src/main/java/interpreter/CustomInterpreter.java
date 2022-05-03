package interpreter;
import java.io.File;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

import org.florresoagli.printscript.*;


public class CustomInterpreter implements PrintScriptInterpreter{


    @Override
    public void execute(File src, String version, org.florresoagli.printscript.Observer emitter, org.florresoagli.printscript.Observer handler) {
        Cli cli = new Cli();
        cli.run(new FileReaderAdapter(src), version, new ExecutionMode(emitter, handler));
    }


}
