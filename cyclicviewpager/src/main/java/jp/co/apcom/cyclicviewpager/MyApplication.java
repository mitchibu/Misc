package jp.co.apcom.cyclicviewpager;

import android.support.v7.app.AppCompatDelegate;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

import jp.co.apcom.cyclicviewpager.dagger.DaggerAppComponent;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;

public class MyApplication extends DaggerApplication {
	static {
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
	}

	@Inject
	public MyDatabase db;

	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		return DaggerAppComponent
				.builder()
				.application(this)
				.create(this);
	}
}
