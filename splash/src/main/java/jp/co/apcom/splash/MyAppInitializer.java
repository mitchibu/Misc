package jp.co.apcom.splash;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MyAppInitializer extends AppInitializer<Integer> {
	@Override
	protected Observable<Integer> getInitializer() {
		return Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> e) throws Exception {
				android.util.Log.v("test", "start");
				Thread.sleep(5000);
				e.onNext(10);
				android.util.Log.v("test", "end");
			}
		});
	}
}
