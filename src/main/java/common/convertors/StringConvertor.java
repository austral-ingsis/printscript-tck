package common.convertors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class StringConvertor {
  public static String convert(InputStream inputStream) {
    try {
      return new String(inputStream.readAllBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String convertLinterConfig(InputStream inputStream) {
    try {
      String theirConfig = new String(inputStream.readAllBytes());
      return adaptConfig(theirConfig);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String adaptConfig(String theirConfig) {
    JsonObject result = new JsonObject();
    JsonObject config = JsonParser.parseString(theirConfig).getAsJsonObject();

    JsonObject identifier = new JsonObject();
    JsonObject writingConvention = new JsonObject();
    try {
      String identifierRules = config.get("identifier_format").getAsString();
      switch (identifierRules) {
        case "snake case" -> {
          writingConvention.addProperty("conventionName", "snakeCase");
          writingConvention.addProperty("conventionPattern", "^[a-z]+(_[a-z0-9]+)*$");
        }
        case "camel case" -> {
          writingConvention.addProperty("conventionName", "camelCase");
          writingConvention.addProperty("conventionPattern", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
        }
        default -> throw new IllegalArgumentException("Invalid identifier format: " + identifierRules);
      }
    } catch (Exception e) {
      writingConvention.addProperty("conventionName", "anyCase");
      writingConvention.addProperty("conventionPattern", "^[a-z]+(_[a-z0-9]+)*$|^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    }
    identifier.add("writingConvention", writingConvention);
    result.add("identifier", identifier);

    JsonObject callExpression = new JsonObject();
    JsonArray arguments = new JsonArray();
    arguments.add("IDENTIFIER");
    arguments.add("STRING_LITERAL");
    arguments.add("NUMBER_LITERAL");
    try {
      boolean strict = config.get("mandatory-variable-or-literal-in-println").getAsBoolean();
      if (!strict) {
        arguments.add("ASSIGNMENT_EXPRESSION");
        arguments.add("BINARY_EXPRESSION");
        arguments.add("CALL_EXPRESSION");
      }
    } catch (Exception e) {
      arguments.add("ASSIGNMENT_EXPRESSION");
      arguments.add("BINARY_EXPRESSION");
      arguments.add("CALL_EXPRESSION");
    }
    callExpression.add("arguments", arguments);
    result.add("callExpression", callExpression);

    return result.toString();
  }
}
