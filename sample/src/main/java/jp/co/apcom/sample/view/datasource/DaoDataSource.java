package jp.co.apcom.sample.view.datasource;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DaoDataSource<T> extends ListDataSource<T> {
	private final Observable<List<T>> observable;

	private Disposable disposable = null;

	public DaoDataSource(@NonNull Observable<List<T>> observable) {
		this.observable = observable;
	}

	@Override
	public void release() {
		if(disposable == null || disposable.isDisposed()) return;
		disposable.dispose();
		disposable = null;
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		disposable = observable
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<List<T>>() {
					@Override
					public void accept(List<T> t) {
						update(t);
					}
				});
	}

	@Override
	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
		release();
	}
}
