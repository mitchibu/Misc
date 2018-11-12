package jp.co.apcom.dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {
	@ContributesAndroidInjector
	abstract MainActivity contributeMainActivity();

	@ContributesAndroidInjector
	abstract MainFragment contributeMainFragment();
}
