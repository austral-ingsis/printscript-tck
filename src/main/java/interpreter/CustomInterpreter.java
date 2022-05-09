package interpreter;
import java.io.File;


import org.florresoagli.printscript.*;


public class CustomInterpreter implements PrintScriptInterpreter{





    @Override
    public void execute(File src, String version,org.florresoagli.printscript.Observer emitter, org.florresoagli.printscript.Observer handler, org.florresoagli.printscript.Reader  provider) {
       org.florresoagli.printscript.CompilerRunner runner = new org.florresoagli.printscript.CompilerRunner();
        runner.run(new FileReaderAdapter(src), version, new ExecutionMode(emitter, handler), provider);
    }
}
