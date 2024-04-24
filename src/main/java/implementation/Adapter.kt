package implementation

import cli.Cli
import cli.Version
import interpreter.InputAdapter
import interpreter.OutputAdapter
import java.io.InputStream

class Adapter {
    fun execute(
        inputStream: InputStream,
        version: String,
        outputAdapter: OutputAdapter,
        inputAdapter: InputAdapter
    ) {
        val cli = startCli(outputAdapter, version, inputAdapter)
        cli.executeInputStream(inputStream)
    }

    private fun startCli(outputAdapter: OutputAdapter, version: String, inputAdapter: InputAdapter): Cli {
        if (version == "v1") {
            return Cli(outputAdapter, Version.VERSION_1, inputAdapter)
        }
        return Cli(outputAdapter, Version.VERSION_2, inputAdapter)
    }
}
