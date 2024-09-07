package implementation

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class JsonCreator {
    fun getJsonFromInputStream(inputStream: InputStream): JsonObject {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = bufferedReader.readText()
        return Json.parseToJsonElement(jsonString).jsonObject
    }
}