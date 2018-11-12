package jp.co.apcom.dagger2;

import android.app.Application;

import javax.inject.Inject;

public class LoginActivityUtil {
	public final Application app;

	@Inject
	public LoginActivityUtil(Application app) {
		this.app = app;
	}
}
