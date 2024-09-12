package adapter.linter

import com.fasterxml.jackson.databind.ObjectMapper
import config.ConfigLoader
import config.VerificationConfig
import java.io.InputStream

class ConfigLoaderAdapter(private val inputStream: InputStream) : ConfigLoader {
    override fun loadConfig(): VerificationConfig {
        val mapper = ObjectMapper()
        return mapper.readValue(inputStream, VerificationConfig::class.java)
    }
}