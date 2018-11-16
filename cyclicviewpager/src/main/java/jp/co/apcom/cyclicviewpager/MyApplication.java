package jp.co.apcom.cyclicviewpager;

import android.support.v7.app.AppCompatDelegate;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

import jp.co.apcom.cyclicviewpager.dagger.DaggerAppComponent;

public class MyApplication extends DaggerApplication {
	static {
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
	}
	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		return DaggerAppComponent
				.builder()
				.application(this)
				.create(this);
	}
}
