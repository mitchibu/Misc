package jp.co.apcom.dagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.dagger2.test.Dog;
import jp.co.apcom.dagger2.test.Pet;

@Module
public class TestModule {
	@Provides
	@Singleton
	public Pet providePet() {
		return new Dog();
	}
}
