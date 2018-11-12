package jp.co.apcom.rx;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
	CompositeDisposable disposables = new CompositeDisposable();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		RxPermission.request(getSupportFragmentManager(), Manifest.permission.ACCESS_FINE_LOCATION).subscribe();
		int requestCode = savedInstanceState == null ? 0 : 1;
		if(savedInstanceState != null) return;
		//requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);

		disposables.add(test()
				.takeUntil(Observable.timer(2, TimeUnit.SECONDS))
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) throws Exception {
						android.util.Log.v("test", "accept");
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
					}
				})
		);

		final Test t = new Test();
		t.init3().subscribeOn(Schedulers.io()).subscribe(new Consumer() {
			@Override
			public void accept(Object o) throws Exception {
				android.util.Log.v("test", "1111");
			}
		});
		t.init3().subscribeOn(Schedulers.io()).subscribe(new Consumer() {
			@Override
			public void accept(Object o) throws Exception {
				android.util.Log.v("test", "2222");
			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				t.init3().subscribeOn(Schedulers.io()).subscribe(new Consumer() {
					@Override
					public void accept(Object o) throws Exception {
						android.util.Log.v("test", "3333");
					}
				});
			}
		}, 1300);
/*		t.init().subscribe(new Consumer() {
			@Override
			public void accept(Object o) throws Exception {
				android.util.Log.v("test", "1111");
			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				t.test().subscribe(new Consumer() {
					@Override
					public void accept(Object o) throws Exception {
						android.util.Log.v("test", "2222");
					}
				});
			}
		}, 1300);*/
//		t.test().subscribe(new Consumer() {
//			@Override
//			public void accept(Object o) throws Exception {
//				android.util.Log.v("test", "2222");
//			}
//		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		disposables.dispose();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		android.util.Log.v("test", "requestCode: " + requestCode);
	}

	Observable<Object> test() {
		return Observable.create(new ObservableOnSubscribe<Object>() {
			@Override
			public void subscribe(ObservableEmitter e) throws Exception {
				e.setCancellable(new Cancellable() {
					@Override
					public void cancel() throws Exception {
						android.util.Log.v("test", "cancel");
					}
				});
			}
		});
	}
}
