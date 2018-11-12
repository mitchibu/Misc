package jp.co.apcom.validator;

public class LengthRangeRule extends Rule<String> {
	private final int max;
	private final int min;

	public LengthRangeRule(int min, int max, CharSequence message) {
		super(message);
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean isValid(String o) {
		if(o == null) o = "";
		int n = o.length();
		return n >= min && n <= max;
	}
}
