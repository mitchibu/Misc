package jp.co.apcom.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static java.lang.Thread.*;

public class Test {
	public final Observable<Object> test = Observable.create(new ObservableOnSubscribe<Object>() {
		@Override
		public void subscribe(ObservableEmitter<Object> e) throws Exception {
			android.util.Log.v("test", "start");
			sleep(1000);
			e.onNext(new Object());
			subject.onNext(new Object());
			android.util.Log.v("test", "end");
		}
	}).publish().autoConnect();

	private final BehaviorSubject<Object> subject = BehaviorSubject.create();

	public Observable init3() {
		return Observable.ambArray(test, subject);
		//return Observable.merge(test, subject);
	}

	Thread t = null;
	public Observable init2() {
		if(t == null) {
			t = new Thread() {
				@Override
				public void run() {
					android.util.Log.v("test", "start");
					try {
						sleep(1000);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					subject.onNext(new Object());
					android.util.Log.v("test", "end");
				}
			};
			t.start();
		}
		return subject;
	}
	Disposable d;
	public Observable init() {
		android.util.Log.v("test", "init");
		Observable o = test();
		//d = test.connect();
		return o;
	}

	public Observable test() {
		android.util.Log.v("test", "test");
		return test;
	}
}
