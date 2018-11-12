package jp.co.apcom.sampleandroidx;

import android.app.Application;

import jp.co.apcom.sampleandroidx.dagger.AppComponent;
import jp.co.apcom.sampleandroidx.dagger.AppModule;
import jp.co.apcom.sampleandroidx.dagger.DaggerAppComponent;
import jp.co.apcom.sampleandroidx.dagger.DbModule;

public class MyApplication extends Application {
	public AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		appComponent = DaggerAppComponent
				.builder()
				.appModule(new AppModule(this))
				.dbModule(new DbModule())
				.build();
	}
}
