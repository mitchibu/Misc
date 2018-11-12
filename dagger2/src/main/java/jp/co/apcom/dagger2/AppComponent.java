package jp.co.apcom.dagger2;

import android.app.Activity;
import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
/*
@Singleton
@Component(modules = {
		AndroidSupportInjectionModule.class,
		AppModule.class,
		ActivityBuilderModule.class
})
public interface AppComponent extends AndroidInjector<MyApplication> {
	@Component.Builder
	abstract class Builder extends AndroidInjector.Builder<MyApplication> {
		@BindsInstance
		abstract Builder application(Application application);
	}

	void inject(MainFragment instance);
}
*/
@Singleton
@Component(modules = {AppModule.class, TestModule.class})
public interface AppComponent {
/*	@Component.Builder
	interface Builder {
//		@BindsInstance
//		Builder application(Application application);

		AppComponent build();
		@BindsInstance
		Builder appModule(AppModule bm);
		@BindsInstance
		Builder testModule(TestModule fm);
	}*/
	void inject(MainFragment instance);
	void inject(MainActivity instance);
//	void inject(MyApplication instance);
}
