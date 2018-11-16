package jp.co.apcom.cyclicviewpager.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jp.co.apcom.cyclicviewpager.MainActivity;

@Module
abstract class ActivityBuilderModule {
	@ContributesAndroidInjector(modules = {MainActivityModule.class})
	abstract MainActivity contributeMainActivity();
}
