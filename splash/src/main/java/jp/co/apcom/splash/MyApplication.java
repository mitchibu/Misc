package jp.co.apcom.splash;

import android.annotation.SuppressLint;
import android.app.Application;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyApplication extends Application {
	private static volatile MyApplication instance;

	public static MyApplication getInstance() {
		return instance;
	}

	public static synchronized MyAppInitializer getInitializer() {
		if(getInstance().appInitializer == null) {
			getInstance().appInitializer = new MyAppInitializer();
		}
		return getInstance().appInitializer;
	}

	public static Observable<Integer> init() {
		return getInitializer().init();
	}

	private MyAppInitializer appInitializer = null;

	@SuppressLint("CheckResult")
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		//noinspection ResultOfMethodCallIgnored
		init().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(data -> android.util.Log.v("test", "init: " + data), Throwable::printStackTrace);
	}
}
