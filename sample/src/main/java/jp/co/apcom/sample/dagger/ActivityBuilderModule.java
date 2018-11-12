package jp.co.apcom.sample.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jp.co.apcom.sample.MainActivity;
import jp.co.apcom.sample.SampleActivity;

@Module
abstract class ActivityBuilderModule {
	@ContributesAndroidInjector(modules = {MainActivityModule.class})
	abstract MainActivity contributeMainActivity();
	@ContributesAndroidInjector
	abstract SampleActivity contributeSampleActivity();
}
