package jp.co.apcom.dagger2.test;

import android.app.Application;

import javax.inject.Inject;

import jp.co.apcom.dagger2.MyApplication;

public class Dog implements Pet {
	public final Application app;

	@Inject
	public Dog(Application app) {this.app = app;}
	public Dog() {this.app = null;}
}
