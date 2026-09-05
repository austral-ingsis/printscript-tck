package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import printscript.interpreter.InterpretationResult;
import printscript.runtime.EnvironmentVariableProvider;
import printscript.runtime.ProgramInput;
import printscript.runtime.ProgramOutput;
import printscript.statement.StatementSource;
import printscript.v1.interpreter.PrintScriptV11InterpreterFactory;
import printscript.v1.interpreter.PrintScriptV1InterpreterFactory;

import java.io.InputStream;
import java.util.Objects;

final class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(
            InputStream source,
            String version,
            PrintEmitter emitter,
            ErrorHandler errorHandler,
            InputProvider inputProvider
    ) {
        Objects.requireNonNull(emitter, "emitter");
        Objects.requireNonNull(errorHandler, "errorHandler");
        Objects.requireNonNull(inputProvider, "inputProvider");

        if (!LanguageVersion.isSupported(version)) {
            errorHandler.reportError(LanguageVersion.unsupportedVersionMessage(version));
            return;
        }

        try {
            executeSupportedVersion(source, version, emitter, errorHandler, inputProvider);
        } catch (OutOfMemoryError error) {
            errorHandler.reportError(error.getMessage());
        }
    }

    private void executeSupportedVersion(
            InputStream source,
            String version,
            PrintEmitter emitter,
            ErrorHandler errorHandler,
            InputProvider inputProvider
    ) {
        final StatementSource statements = PrintScriptPipeline.statementsFrom(source, version);
        final ProgramOutput output = emitter::print;
        final ProgramInput input = prompt -> {
            emitter.print(prompt);
            return inputProvider.input(prompt);
        };
        final EnvironmentVariableProvider environmentVariables = System::getenv;
        final printscript.interpreter.Interpreter interpreter = interpreterFor(
                version,
                output,
                input,
                environmentVariables
        );
        final InterpretationResult result = interpreter.interpret(statements);

        if (result instanceof InterpretationResult.ParseFailure failure) {
            errorHandler.reportError(String.valueOf(failure.getError()));
        } else if (result instanceof InterpretationResult.SemanticFailure failure) {
            errorHandler.reportError(String.valueOf(failure.getError()));
        }
    }

    private printscript.interpreter.Interpreter interpreterFor(
            String version,
            ProgramOutput output,
            ProgramInput input,
            EnvironmentVariableProvider environmentVariables
    ) {
        if (LanguageVersion.isVersionOne(version)) {
            return PrintScriptV1InterpreterFactory.create(output);
        }

        return PrintScriptV11InterpreterFactory.create(output, input, environmentVariables);
    }
}
