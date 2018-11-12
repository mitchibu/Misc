package jp.co.apcom.validator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void notNullRule() {
		Validator validator = new Validator();
		validator.addRule(new NotNullRule(null));
		assertNull(validator.validate("abc"));
		assertNotNull(validator.validate(null));
		assertNull(validator.validate(""));
	}

	@Test
	public void notEmptyRule() {
		Validator validator = new Validator();
		validator.addRule(new NotEmptyRule(null));
		assertNull(validator.validate("abc"));
		assertNotNull(validator.validate(null));
		assertNotNull(validator.validate(""));
	}

	@Test
	public void regexRule() {
		Validator validator = new Validator();
		validator.addRule(new RegexRule("^(\\d{4})(\\d{2})(\\d{4})$", null));
		assertNull(validator.validate("1234567890"));
		assertNotNull(validator.validate(null));
		assertNotNull(validator.validate("abc"));
	}

	@Test
	public void emailAddressRule() {
		Validator validator = new Validator();
		validator.addRule(new EmailAddressRule(null));
		assertNull(validator.validate("m_shimada@ap-com.co.jp"));
		assertNotNull(validator.validate(null));
		assertNotNull(validator.validate("a@test"));
	}

	@Test
	public void lengthRangeRule() {
		Validator validator = new Validator();
		validator.addRule(new LengthRangeRule(0, 4, null));
		assertNull(validator.validate(""));
		assertNull(validator.validate("a"));
		assertNull(validator.validate("abcd"));
		assertNull(validator.validate(null));
		assertNotNull(validator.validate("abcde"));
	}
}