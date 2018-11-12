package jp.co.apcom.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//noinspection ResultOfMethodCallIgnored
		MyApplication.init()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.takeUntil(RxLifecycle.OnDestroyAsObservable(getLifecycle()))
				.subscribe(data -> android.util.Log.v("test", "init: " + data), Throwable::printStackTrace);
	}
}
