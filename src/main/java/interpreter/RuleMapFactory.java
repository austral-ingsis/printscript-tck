package interpreter;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RuleMapFactory {
	private final InputStream config;

	public RuleMapFactory(InputStream config) {
		this.config = config;
	}

	public Map<String, String> getRules() {
		try {
			JSONObject configJson = new JSONObject(new JSONTokener(config));
			config.close();
			return getRuleList(configJson);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, String> getRuleList(JSONObject configJson) {
		Iterator<String> keys = configJson.keys();
        Map<String, String> ruleAppliers = new HashMap<>();
		while (keys.hasNext()) {
			String key = keys.next();
            Map.Entry<String, String> ruleApplier = getRuleApplier(key, configJson);
            ruleAppliers.put(ruleApplier.getKey(), ruleApplier.getValue());
		}
		return ruleAppliers;
	}

	private Map.Entry<String, String> getRuleApplier(String key, JSONObject jsonObject) {
        return switch (key) {
            case "enforce-spacing-before-colon-in-declaration" -> Map.entry("spaceBeforeColon", jsonObject.getString(key));
            case "enforce-spacing-after-colon-in-declaration" -> Map.entry("spaceAfterColon", jsonObject.getString(key));
            case "enforce-no-spacing-around-equals" -> Map.entry("spaceAroundEquals", invertBoolean(jsonObject.getString(key)));
            case "enforce-spacing-around-equals" -> Map.entry("spaceAroundEquals", jsonObject.getString(key));
            case "line-breaks-after-println" -> Map.entry("breaksBeforePrintln", jsonObject.getString(key));
            case "mandatory-space-around-operators" -> Map.entry("spaceAroundOperator", jsonObject.getString(key));
            case "spacesAroundCommasInParameters" -> Map.entry("spacesAroundCommasInParameters", jsonObject.getString(key));
            default -> throw new RuntimeException("Unknown rule: " + key);
        };
    }

    private String invertBoolean (String bool) {
        if (bool.equals("true")) {
            return "false";
        } else {
            return "true";
        }
    }
}
