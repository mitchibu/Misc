package jp.co.apcom.validator;

public class EmailAddressRule extends RegexRule {
	public EmailAddressRule(CharSequence message) {
		super("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?=.*?[.])(?:\\.[a-zA-Z0-9-]+)*$", message);
	}
}
