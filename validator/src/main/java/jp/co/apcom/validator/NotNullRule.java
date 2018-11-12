package jp.co.apcom.validator;

public class NotNullRule extends Rule<Object> {
	public NotNullRule(CharSequence message) {
		super(message);
	}

	@Override
	public boolean isValid(Object o) {
		return o != null;
	}
}
