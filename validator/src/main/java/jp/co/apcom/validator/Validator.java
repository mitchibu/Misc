package jp.co.apcom.validator;

import java.util.ArrayList;
import java.util.List;

public class Validator {
	private final List<Rule<?>> rules = new ArrayList<>();

	public Validator addRule(Rule<?> rule) {
		rules.add(rule);
		return this;
	}

	public Rule validate(Object o) {
		for(Rule rule : rules) {
			if(!rule.isValid(o)) return rule;
		}
		return null;
	}
}
