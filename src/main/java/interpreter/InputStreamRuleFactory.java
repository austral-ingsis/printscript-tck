package interpreter;

import org.example.FormatterRules;
import org.example.factories.RuleFactory;
import org.example.ruleappliers.RuleApplier;
import org.example.ruleappliers.assignation.SpaceAroundEquals;
import org.example.ruleappliers.declaration.SpaceAfterColon;
import org.example.ruleappliers.declaration.SpaceBeforeColon;
import org.example.ruleappliers.expression.SpaceAroundOperator;
import org.example.ruleappliers.function.BreaksBeforePrintln;
import org.example.ruleappliers.parameters.SpacesAroundCommasInParameters;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputStreamRuleFactory implements RuleFactory {
	private final InputStream config;

	public InputStreamRuleFactory(InputStream config) {
		this.config = config;
	}

	@Override
	public FormatterRules getRules() {
		try {
			JSONObject configJson = new JSONObject(new JSONTokener(config));
			config.close();
			return new FormatterRules(getRuleList(configJson));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<RuleApplier<?>> getRuleList(JSONObject configJson) {
		List<RuleApplier<?>> rules = new ArrayList<>();
		Iterator<String> keys = configJson.keys();
		List<RuleApplier<?>> ruleAppliers = new ArrayList<>();
		while (keys.hasNext()) {
			String key = keys.next();
			ruleAppliers.add(getRuleApplier(key, configJson));
		}
		//            Non customizable rules
		addNonCustomizableRules(ruleAppliers);
		return ruleAppliers;
	}

	private RuleApplier<?> getRuleApplier(String key, JSONObject jsonObject) {
		return switch (key) {
			case "enforce-spacing-before-colon-in-declaration" -> new SpaceBeforeColon(jsonObject.getBoolean(key));
			case "enforce-spacing-after-colon-in-declaration" -> new SpaceAfterColon(jsonObject.getBoolean(key));
			case "enforce-no-spacing-around-equals" -> new SpaceAroundEquals(!jsonObject.getBoolean(key));
			case "enforce-spacing-around-equals" -> new SpaceAroundEquals(jsonObject.getBoolean(key));
			case "line-breaks-after-println" -> new BreaksBeforePrintln(jsonObject.getInt(key));
			case "mandatory-space-around-operators" -> new SpaceAroundOperator(jsonObject.getBoolean(key));
			case "spacesAroundCommasInParameters" ->
					new SpacesAroundCommasInParameters(jsonObject.getBoolean(key));
			default -> throw new RuntimeException("Unknown rule: " + key);
		};
	}

	private void addNonCustomizableRules(List<RuleApplier<?>> ruleAppliers) {
		ruleAppliers.add(new SpaceAroundOperator(true));
	}
}
