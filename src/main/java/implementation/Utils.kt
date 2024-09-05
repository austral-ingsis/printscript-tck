package implementation

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.InputStream

class Utils {
    fun getJsonFromStream(stream: InputStream): JsonObject {
        val content = stream.bufferedReader().use { it.readText() }
        return Json.parseToJsonElement(content).jsonObject
    }
}