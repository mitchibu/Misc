package jp.co.apcom.lifecycle;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.co.apcom.lifecycle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	MyViewModel m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		m = ViewModelProviders.of(this).get(MyViewModel.class);
		binding.setModel(m);
//		test1.observe(this, new Observer<Integer>() {
//			@Override
//			public void onChanged(@Nullable Integer integer) {
//				android.util.Log.v("test", "test1: " + test1.getValue());
//			}
//		});
		if(savedInstanceState == null) {
			m.test1.setValue(0);
			m.test2.setValue(new Data());
		} else {
			m.test1.setValue(m.test1.getValue() + 1);
			m.test2.setValue(new Data(m.test2.getValue().a + 1));
		}
		android.util.Log.v("test", "test1: " + m.test1.getValue());
		android.util.Log.v("test", "test2: " + m.test2.getValue().a);
	}
}
