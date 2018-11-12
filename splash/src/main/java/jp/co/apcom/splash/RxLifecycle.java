package jp.co.apcom.splash;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@SuppressWarnings("Convert2Lambda")
public class RxLifecycle {
	public static Observable<Lifecycle.Event> OnCreateAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_CREATE));
	}

	public static Observable<Lifecycle.Event> OnDestroyAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_DESTROY));
	}

	public static Observable<Lifecycle.Event> OnPauseAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_PAUSE));
	}

	public static Observable<Lifecycle.Event> OnResumeAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_RESUME));
	}

	public static Observable<Lifecycle.Event> OnStartAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_START));
	}

	public static Observable<Lifecycle.Event> OnStopAsObservable(Lifecycle lifecycle) {
		return new RxLifecycle(lifecycle).onEventAsObservable().filter(eventFilter(Lifecycle.Event.ON_STOP));
	}

	private static Predicate<Lifecycle.Event> eventFilter(Lifecycle.Event target) {
		return new Predicate<Lifecycle.Event>() {
			@Override
			public boolean test(Lifecycle.Event event) {
				return event.equals(target);
			}
		};
	}

	private final Lifecycle lifecycle;
	private final Subject<Lifecycle.Event> subject = PublishSubject.<Lifecycle.Event>create().toSerialized();
	private final LifecycleObserver lifecycleObserver = new LifecycleObserver() {
		@SuppressWarnings("unused")
		@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
		void onAny(LifecycleOwner source, Lifecycle.Event event) {
			subject.onNext(event);
		}
	};

	private RxLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
		lifecycle.addObserver(lifecycleObserver);
	}

	private Observable<Lifecycle.Event> onEventAsObservable() {
		return subject.flatMap(new Function<Lifecycle.Event, ObservableSource<Lifecycle.Event>>() {
			@Override
			public ObservableSource<Lifecycle.Event> apply(Lifecycle.Event event) {
				return Observable.create(new ObservableOnSubscribe<Lifecycle.Event>() {
					@Override
					public void subscribe(ObservableEmitter<Lifecycle.Event> emitter) {
						if(emitter.isDisposed()) return;
						emitter.onNext(event);
						emitter.setCancellable(new Cancellable() {
							@Override
							public void cancel() {
								lifecycle.removeObserver(lifecycleObserver);
							}
						});
					}
				});
			}
		});
	}
}
