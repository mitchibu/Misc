package jp.co.apcom.sample;

import android.annotation.SuppressLint;
import android.app.Application;

import java.util.concurrent.TimeUnit;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import jp.co.apcom.processor.Test;
import jp.co.apcom.sample.dagger.DaggerAppComponent;

// https://medium.com/@star_zero/dagger-android%E6%8B%A1%E5%BC%B5%E3%81%AE%E4%BD%BF%E3%81%84%E6%96%B9-6527dcb74531
@Test
public class MyApplication extends DaggerApplication {
	public static Observable<InitialData> initialize(Application app) {
		return ((MyApplication)app).initialize();
	}

	final BehaviorSubject<InitialData> initialSubject = BehaviorSubject.create();

	Observable<InitialData> initialObserver = null;

	@SuppressLint("CheckResult")
	@Override
	public void onCreate() {
		super.onCreate();
		AutoInjector.register(this);

		//noinspection ResultOfMethodCallIgnored
		initialize(this)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(data -> {}, Throwable::printStackTrace);
	}

	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		return DaggerAppComponent
				.builder()
				.application(this)
				.create(this);
	}

	private synchronized Observable<InitialData> initialize() {
		if(initialObserver == null) {
			initialObserver = initialMain().map(data -> {
				initialSubject.onNext(data);
				return data;
			}).publish().autoConnect();
		}
		return initialSubject.ambWith(initialObserver);
	}

	private Observable<InitialData> initialMain() {
		return Observable.just(new InitialData())
				.delay(5, TimeUnit.SECONDS);
	}

	public static class InitialData {
	}
}
