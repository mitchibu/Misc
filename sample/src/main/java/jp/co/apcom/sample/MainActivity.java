package jp.co.apcom.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.ArrayList;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import jp.co.apcom.sample.databinding.ActivityMainBinding;
import jp.co.apcom.sample.dialog.Dialog;

public class MainActivity extends DaggerAppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setSplashClick(view -> startActivity(new Intent(this, SplashActivity.class)));
		binding.setSampleClick(view -> startActivity(new Intent(this, SampleActivity.class)));
		binding.setDialogClick(view -> Dialog.show(this, null, Dialog.CODE_TEST));
		binding.setFragmentClick(view -> getSupportFragmentManager().beginTransaction().add(new MainFragment(), null).commit());

		ArrayList<Integer> a = Observable.just(1, 2, 3)
				.map(v -> v * 2)
				.collectInto(new ArrayList<Integer>(), ArrayList::add)
				.blockingGet();
				//.blockingSubscribe(v -> android.util.Log.v("test", "res: " + v));
		Observable.fromIterable(a)
				.blockingSubscribe(v -> android.util.Log.v("test", "res: " + v));
	}
}
