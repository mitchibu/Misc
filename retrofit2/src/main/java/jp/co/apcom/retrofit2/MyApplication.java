package jp.co.apcom.retrofit2;

import android.app.Application;

public class MyApplication extends Application {
	private static volatile MyApplication instance;

	public Client client;

	public static MyApplication getInstance() {
		return instance;
	}

	public static Client client() {
		return getInstance().client;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		client = new Client(this);
	}
}
