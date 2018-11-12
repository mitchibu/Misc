package jp.co.apcom.validator;

public class NotEmptyRule extends RegexRule {
	public NotEmptyRule(CharSequence message) {
		super(".+", message);
	}
}
