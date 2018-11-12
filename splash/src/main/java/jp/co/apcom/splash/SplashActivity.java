package jp.co.apcom.splash;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme_Splash);
		super.onCreate(savedInstanceState);

		//noinspection ResultOfMethodCallIgnored
		Observable.timer(2, TimeUnit.SECONDS)
				.zipWith(MyApplication.init(), (n, data) -> data)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.takeUntil(RxLifecycle.OnPauseAsObservable(getLifecycle()))
				.map(this::createIntent)
				.subscribe(this::next, this::error);
	}

	@Override
	public void onBackPressed() {
	}

	private Intent createIntent(Integer data) {
		Intent me = getIntent();
		Bundle extras = me.getExtras();
//		Intent i = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP).setData(me.getData());
//		Intent i = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP).setData(me.getData());
		Intent i = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TOP).setData(me.getData());
		if(extras != null) i.putExtras(extras);
		return i;
	}

	private void next(Intent intent) {
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	private void error(Throwable t) {
		new AlertDialog.Builder(this)
				.setMessage("err: " + t.getMessage())
				.setPositiveButton("ok", (dialog, which) -> finish())
				.show();
	}
}
