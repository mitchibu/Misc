package jp.co.apcom.dagger2;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MyApplication extends Application/* implements HasActivityInjector */{
//	@Inject
//	DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

	public AppComponent appComponent;
	@Override
	public void onCreate() {
		super.onCreate();
		appComponent = DaggerAppComponent
				.builder()
				//.application(this)
				.appModule(new AppModule(this))
				.testModule(new TestModule())
				.build();
				//.inject(this);
	}

//	@Override
//	public AndroidInjector<Activity> activityInjector() {
//		return dispatchingAndroidInjector;
//	}
}

/*
public class MyApplication extends DaggerApplication {
	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		return DaggerAppComponent
				.builder()
				.application(this)
				.create(this);
	}
}*/