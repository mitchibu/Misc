package jp.co.apcom.sampleandroidx;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import jp.co.apcom.sampleandroidx.db.MyDatabase;

public class MainActivity extends AppCompatActivity {
	private MyDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		((MyApplication)getApplication()).appComponent.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
