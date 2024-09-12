package implementation.linter

import kotlinx.serialization.json.*

class CheckAdapter {

    fun adapt(theirJson: JsonObject): JsonObject {
        val resultMap = mutableMapOf<String, JsonElement>()

        theirJson.forEach { (key, value) ->
            when (key) {
                "mandatory-variable-or-literal-in-readInput" -> {
                    resultMap["ReadInputCheck"] =
                        JsonObject(mapOf("readInputCheckEnabled" to value))
                }
                "identifier_format" -> {
                    val namingPattern = when (value.jsonPrimitive.content) {
                        "snake case" -> "snake_case"
                        "camel case" -> "camelCase"
                        else -> throw IllegalArgumentException("Unknown naming pattern:" +
                                " ${value.jsonPrimitive.content}")
                    }
                    resultMap["NamingFormatCheck"] =
                        JsonObject(mapOf("namingPatternName" to JsonPrimitive(namingPattern)))
                }
                "mandatory-variable-or-literal-in-println" -> {
                    resultMap["PrintUseCheck"] =
                        JsonObject(mapOf("printlnCheckEnabled" to value))
                }
            }
        }

        return JsonObject(resultMap)
    }
}