package jp.co.apcom.validator;

public class RegexRule extends Rule<String> {
	private final String regex;

	public RegexRule(String regex, CharSequence message) {
		super(message);
		this.regex = regex;
	}

	@Override
	public boolean isValid(String o) {
		return o != null && o.matches(regex);
	}
}
