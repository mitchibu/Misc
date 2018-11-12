package jp.co.apcom.dagger2;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

//import dagger.android.InjectionUtils;
import jp.co.apcom.dagger2.test.Pet;

public class MainActivity extends AppCompatActivity {
	@Inject
	Application app;
	@Inject
	LoginActivityUtil util;
	@Inject
	Pet pet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		InjectionUtils.inject(this);
//		((MyApplication)getApplication()).appComponent.inject(this);
		InjectionUtils.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		((MyApplication)getApplication()).getAppComponent().inject(this);
		android.util.Log.v("test", "app: " + app);
		android.util.Log.v("test", "util: " + util);
		android.util.Log.v("test", "util.app: " + util.app);
		android.util.Log.v("test", "pet: " + pet);

		findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
			}
		});
	}
}
