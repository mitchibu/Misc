package jp.co.apcom.dagger2;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.dagger2.test.Dog;
import jp.co.apcom.dagger2.test.Pet;

@Module
public class AppModule {
	MyApplication app;

	public AppModule(MyApplication app) {
		this.app = app;
	}

	@Provides
	@Singleton
	public Application provideApplication() {
		return app;
	}
//	@Provides
//	@Singleton
//	public Dog provideDog() {
//		return new Dog();
//	}

//	@Provides
//	@Singleton
//	public Pet providePet1() {
//		return new Dog();
//	}
}
