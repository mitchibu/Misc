package jp.co.apcom.sampleandroidx.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.sampleandroidx.MyApplication;

@Module
public class AppModule {
	MyApplication app;

	public AppModule(MyApplication app) {
		this.app = app;
	}

	@Provides
	@Singleton
	public MyApplication provideMyApplication() {
		return app;
	}
}
