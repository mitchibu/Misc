package jp.co.apcom.validator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Validator validator = new Validator();
		validator.addRule(new NotEmptyRule(null));
		android.util.Log.v("test", "1: " + (validator.validate(null)));
		android.util.Log.v("test", "2: " + (validator.validate("")));
		android.util.Log.v("test", "3: " + (validator.validate("abc")));
	}
}
