package implementation

import cli.Cli
import cli.Version
import interpreter.OutputAdapter
import java.io.InputStream

class Adapter {
    fun execute(
        inputStream: InputStream,
        version: String,
        outputAdapter: OutputAdapter,
    ) {
        val cli = startCli(outputAdapter, version)
        cli.executeInputStream(inputStream)
    }

    private fun startCli(outputAdapter: OutputAdapter, version: String): Cli {
        if (version == "v1") {
            return Cli(outputAdapter, Version.VERSION_1)
        }
        return Cli(outputAdapter, Version.VERSION_1)
    }
}
