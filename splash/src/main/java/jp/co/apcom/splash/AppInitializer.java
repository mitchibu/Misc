package jp.co.apcom.splash;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public abstract class AppInitializer<T> {
	private final BehaviorSubject<T> subject = BehaviorSubject.create();

	private Observable<T> initializer = null;

	public Observable<T> init() {
		if(initializer == null) {
			initializer = getInitializer().map(new Function<T, T>() {
				@Override
				public T apply(T integer) throws Exception {
					subject.onNext(integer);
					return integer;
				}
			}).publish().autoConnect();
		}
		return Observable.amb(Arrays.asList(initializer, subject));
	}

	protected abstract Observable<T> getInitializer();
}
