package jp.co.apcom.sample.dagger;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import jp.co.apcom.sample.MainActivity;
import jp.co.apcom.sample.MainFragment;

@Module
abstract class MainActivityModule {
	@ContributesAndroidInjector
	abstract MainFragment contributeMainFragment();

	@Binds
	abstract Activity bindsActivity(MainActivity activity);

	@Provides
	static FragmentManager provideFragmentManager(MainActivity activity) {
		return activity.getSupportFragmentManager();
	}
}
