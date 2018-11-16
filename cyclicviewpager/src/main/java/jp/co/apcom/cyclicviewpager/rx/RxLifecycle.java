package jp.co.apcom.cyclicviewpager.rx;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Predicate;

public class RxLifecycle {
	public static Observable<Lifecycle.Event> OnCreateAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_CREATE));
	}

	public static Observable<Lifecycle.Event> OnDestroyAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_DESTROY));
	}

	public static Observable<Lifecycle.Event> OnPauseAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_PAUSE));
	}

	public static Observable<Lifecycle.Event> OnResumeAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_RESUME));
	}

	public static Observable<Lifecycle.Event> OnStartAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_START));
	}

	public static Observable<Lifecycle.Event> OnStopAsObservable(Lifecycle lifecycle) {
		return onEventAsObservable(lifecycle).filter(eventFilter(Lifecycle.Event.ON_STOP));
	}

	private static Predicate<Lifecycle.Event> eventFilter(final Lifecycle.Event target) {
		return new Predicate<Lifecycle.Event>() {
			@Override
			public boolean test(Lifecycle.Event event) {
				return event.equals(target);
			}
		};
	}

	private static Observable<Lifecycle.Event> onEventAsObservable(final Lifecycle lifecycle) {
		return Observable.create(new ObservableOnSubscribe<Lifecycle.Event>() {
			@Override
			public void subscribe(final ObservableEmitter<Lifecycle.Event> emitter) {
				final LifecycleObserver lifecycleObserver = new LifecycleObserver() {
					@SuppressWarnings("unused")
					@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
					void onAny(LifecycleOwner source, Lifecycle.Event event) {
						emitter.onNext(event);
					}
				};
				lifecycle.addObserver(lifecycleObserver);
				emitter.setCancellable(new Cancellable() {
					@Override
					public void cancel() {
						android.util.Log.v("test", "removeObserver");
						lifecycle.removeObserver(lifecycleObserver);
					}
				});
			}
		});
	}
}
