package jp.co.apcom.validator;

public abstract class Rule<T> {
	private final CharSequence message;

	protected Rule(CharSequence message) {
		this.message = message;
	}

	public CharSequence getMessage() {
		return message;
	}

	public abstract boolean isValid(T o);
}
