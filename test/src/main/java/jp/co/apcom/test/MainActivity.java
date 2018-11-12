package jp.co.apcom.test;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jp.co.apcom.test.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Model model = ViewModelProviders.of(this).get(Model.class);

		ActivityMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setLifecycleOwner(this);
		binding.setModel(model);
		binding.test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//model.sketchColor.postValue(Color.RED);
				new PenSettingsFragment().show(getSupportFragmentManager(), null);
			}
		});
	}
}
