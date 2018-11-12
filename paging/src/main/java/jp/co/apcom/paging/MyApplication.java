package jp.co.apcom.paging;

import android.app.Application;

public class MyApplication extends Application {
	MyDatabase db;

	@Override
	public void onCreate() {
		super.onCreate();
		db = MyDatabase.getInstance(this);
	}
}
