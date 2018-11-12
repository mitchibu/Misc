package jp.co.apcom.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.co.apcom.sample.dialog.AlertDialogFragment;
import jp.co.apcom.sample.rx.RxLifecycle;

public class SplashActivity extends AppCompatActivity implements AlertDialogFragment.OnClickListener {
	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme_Splash);
		super.onCreate(savedInstanceState);

		//noinspection ResultOfMethodCallIgnored
		Observable.timer(2, TimeUnit.SECONDS)
				.zipWith(MyApplication.initialize(getApplication()), (n, data) -> data)
				.map(this::createIntent)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this::next, this::error);
	}

	@Override
	public void onBackPressed() {
	}

	private Intent createIntent(MyApplication.InitialData data) {
		Intent me = getIntent();
		Bundle extras = me.getExtras();
		Intent i = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP).setData(me.getData());
		if(extras != null) i.putExtras(extras);
		return i;
	}

	private void next(Intent intent) {
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	private void error(Throwable t) {
		new AlertDialogFragment.Builder(this)
				.message("err: " + t.getMessage())
				.positive("ok")
				.show(null);
	}

	@Override
	public void onClick(Fragment dialog, int which, Bundle params) {
		finish();
	}
}
