package jp.co.apcom.sampleandroidx.dagger;

import javax.inject.Singleton;

import dagger.Component;
import jp.co.apcom.sampleandroidx.MainActivity;

@Singleton
@Component(modules = {AppModule.class, DbModule.class})
public interface AppComponent {
	void inject(MainActivity instance);
}
