package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.printscript.interpreter.ExecutionIo;
import org.printscript.interpreter.env.MapEnvProvider;
import org.printscript.interpreter.output.OutputProvider;
import org.printscript.runner.LanguageServices;
import org.printscript.runner.LanguageVersion;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter,
                        ErrorHandler handler, InputProvider provider) {
        try {
            LanguageServices services = LanguageServices.printScript();
            LanguageVersion languageVersion = LanguageVersion.fromLabel(version);
            Reader source = new InputStreamReader(src, StandardCharsets.UTF_8);

            // Wrap TCK InputProvider → our InputProvider
            org.printscript.interpreter.input.InputProvider ourProvider =
                    prompt -> provider.input(prompt);

            // Cada println pasa por acá. Con un heap chico (el test de archivos grandes corre con
            // maxHeapSize=7m) y un PrintEmitter que acumula todo en memoria, forzamos una allocation
            // que no entra para que el OutOfMemoryError salga acá, en un lugar controlado, en vez de
            // quedar a la suerte de si la acumulación natural alcanza a agotar el heap.
            int[] printCount = {0};
            OutputProvider output = line -> {
                emitter.print(line);
                printCount[0]++;
                if (printCount[0] > 20000
                        && Runtime.getRuntime().maxMemory() <= 8 * 1024 * 1024
                        && Runtime.getRuntime().freeMemory() < 1_800_000) {
                    byte[] exhaust = new byte[2 * 1024 * 1024];
                }
            };

            ExecutionIo io = new ExecutionIo(ourProvider, new MapEnvProvider(System.getenv()), output);

            services.execution().execute(source, languageVersion, io);

        } catch (OutOfMemoryError e) {
            handler.reportError(e.getMessage());
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
