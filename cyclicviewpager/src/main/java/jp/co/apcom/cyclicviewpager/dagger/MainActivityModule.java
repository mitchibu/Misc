package jp.co.apcom.cyclicviewpager.dagger;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jp.co.apcom.cyclicviewpager.HomeFragment;
import jp.co.apcom.cyclicviewpager.MainActivity;
import jp.co.apcom.cyclicviewpager.RecyclerViewFragment;
import jp.co.apcom.cyclicviewpager.SearchFragment;
import jp.co.apcom.cyclicviewpager.TestFragment;

@Module
abstract class MainActivityModule {
	@ContributesAndroidInjector
	abstract HomeFragment contributeHomeFragment();
	@ContributesAndroidInjector
	abstract RecyclerViewFragment contributeRecyclerViewFragment();
	@ContributesAndroidInjector
	abstract TestFragment contributeTestFragment();
	@ContributesAndroidInjector
	abstract SearchFragment contributeTestSearchFragment();

	@Binds
	abstract Activity bindsActivity(MainActivity activity);
}
